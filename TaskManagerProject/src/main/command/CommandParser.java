package main.command;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import data.taskinfo.Priority;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class CommandParser {
    private static Map<DateTimeFormatter, String> dateFormatPatterns;
    private static LocalDate datePatternsLastUpdate;

    private final static String SYMBOL_IGNORE = "\"";
    private final static String SYMBOL_TAG = "#";
    private final static String SYMBOL_PRIORITY = "+";
    private final static Priority DEFAULT_PRIORITY = Priority.LOW;

    public static TaskInfo parseTask(String taskText) {
        TaskInfo task = new TaskInfo();

        parseName(taskText, task);
        parseDateTime(taskText, task);
        parseTags(taskText, task);
        parsePriority(taskText, task);

        return task;
    }

    public static void parseName(String args, TaskInfo task) {
        task.name = args;
        // TODO Proper parsing.
    }

    public static void parseDateTime(String args, TaskInfo task) {
        // TODO Time parsing and duration
        args = stripIgnoredSegments(args);
        String[] words = args.split(" ");

        List<LocalDate> dates = new ArrayList<LocalDate>();

        for (String word : words) {
            LocalDate d = parseDate(word);
            if (d != null) {
                dates.add(d);
            }
        }

        if (dates.size() == 1) {
            task.endDate = dates.get(0);
        }
        if (dates.size() > 1) {
            task.endDate = dates.get(1);
        }
    }

    private static LocalDate parseDate(String dateString) {
        if (shouldUpdateDatePatterns()) {
            buildDatePatternHashMap();
        }

        LocalDate d = parseRelativeDate(dateString);
        if (d == null) {
            d = parseAbsoluteDate(dateString);
        }

        return d;
    }

    private static LocalDate parseRelativeDate(String dateString) {
        // TODO weekdays (absolute?), +7d, -7d
        switch (dateString.toLowerCase()) {
            case "yesterday" :
                return LocalDate.now().minusDays(1);
            case "today" :
            case "now" :
                return LocalDate.now();
            case "tomorrow" :
                return LocalDate.now().plusDays(1);
            default :
                return null;
        }
    }

    private static LocalDate parseAbsoluteDate(String dateString) {
        // TODO Fix bug where a string can match numerous patterns.
        LocalDate date = null;

        Iterator<Entry<DateTimeFormatter, String>> i =
            dateFormatPatterns.entrySet().iterator();
        while (i.hasNext()) {
            Entry<DateTimeFormatter, String> formatPattern = i.next();
            DateTimeFormatter format = formatPattern.getKey();
            String missingField = formatPattern.getValue();

            try {
                date = LocalDate.parse(dateString + missingField, format);
            } catch (DateTimeParseException e) {
                // do nothing
            }
        }

        return date;
    }

    private static void buildDatePatternHashMap() {
        dateFormatPatterns = new HashMap<DateTimeFormatter, String>();

        // 24 August
        mapPattern(dateFormatPatterns,
                "d MMMM" + "y", String.valueOf(LocalDate.now().getYear()));
        // 24Aug
        mapPattern(dateFormatPatterns,
                "dMMM" + "y", String.valueOf(LocalDate.now().getYear()));
        // 24Aug2014
        mapPattern(dateFormatPatterns, "dMMMy", null);
        // 24Aug14
        mapPattern(dateFormatPatterns, "dMMMyy", null);
        // 24/8/14
        mapPattern(dateFormatPatterns, "d/M/yy", null);

        datePatternsLastUpdate = LocalDate.now();
    }

    private static boolean shouldUpdateDatePatterns() {
        return dateFormatPatterns == null ||
               !datePatternsLastUpdate.equals(LocalDate.now());
    }

    private static void mapPattern(Map<DateTimeFormatter, String> map,
            String pattern, String missingField) {
        map.put(DateTimeFormatter.ofPattern(pattern), missingField);
    }

    public static void parseTags(String args, TaskInfo task) {
        // TODO Care about efficiency.
        args = stripIgnoredSegments(args);
        String[] words = args.split(" ");

        List<Tag> tags = new ArrayList<Tag>();
        for (String word : words) {
            if (word.startsWith(SYMBOL_TAG)) {
                tags.add(new Tag(removeFirstChar(word)));
            }
        }
        task.tags = (Tag[]) tags.toArray();
    }

    public static void parsePriority(String args, TaskInfo task) {
        // TODO Care about efficiency.
        args = stripIgnoredSegments(args);
        String[] words = args.split(" ");
        for (String word : words) {
            if (word.startsWith(SYMBOL_PRIORITY)) {
                Priority p = DEFAULT_PRIORITY;

                String priorityLevel = removeFirstChar(word).toLowerCase();
                if (priorityLevel.equals("high")) {
                    p = Priority.HIGH;
                }
                if (priorityLevel.equals("med")) {
                    p = Priority.MEDIUM;
                }

                task.priority = p;
            }
        }
    }

    private static String removeFirstChar(String s) {
        return s.substring(1);
    }

    private static String stripIgnoredSegments(String s) {
        StringBuilder sB = new StringBuilder(s);

        boolean shouldCheckFurther;
        do {
            int startIgnoreIdx = s.indexOf(SYMBOL_IGNORE);
            int endIgnoreIdx = s.indexOf(SYMBOL_IGNORE, startIgnoreIdx + 1);

            shouldCheckFurther = endIgnoreIdx > startIgnoreIdx;
            if (shouldCheckFurther) {
                sB.delete(startIgnoreIdx, endIgnoreIdx + 1);
            }
        } while (shouldCheckFurther);

        return sB.toString();
    }

}
