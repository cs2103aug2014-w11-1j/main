package main.command.parser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import data.taskinfo.Priority;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class CommandParser {
    private final static String SYMBOL_DELIM = " ";
    private final static String SYMBOL_IGNORE = "\"";
    private final static String SYMBOL_TAG = "#";
    private final static String SYMBOL_PRIORITY = "+";
    private final static Priority DEFAULT_PRIORITY = null;

    public static TaskInfo parseTask(String taskText) {
        TaskInfo task = TaskInfo.create();

        task.name = parseName(taskText);
        parseDateTime(taskText, task);
        task.tags = parseTags(taskText);
        Priority p = parsePriority(taskText);
        if (p != null) {
            task.priority = p;
        }

        return task;
    }

    public static String parseNameNew(String args) {
        String name = parseNameRecurse(args);
        String cleanedName = cleanCmdString(name);
        return cleanedName;
    }

    private static String parseNameRecurse(String args) {
        if (hasIgnoredSegment(args)) {
            String ignoredSegment = getIgnoredSegment(args);
            int startIgnoreIdx = args.indexOf(ignoredSegment) - 1;
            int endIgnoreIdx = startIgnoreIdx + ignoredSegment.length() + 1;

            // recursively parse non-ignored segments
            String front = parseNameRecurse(args.substring(0, startIgnoreIdx));
            String back = parseNameRecurse(args.substring(endIgnoreIdx));

            String cleanedIgnoredSegment =
                    ignoredSegment.substring(1, ignoredSegment.length() - 1);
            return front + cleanedIgnoredSegment + back;
        } else {
            // remove if should not be part of name
            List<String> tokens = Arrays.asList(args.split(SYMBOL_DELIM));
            for (int i = 0; i < tokens.size(); i++) {
                for (int j = tokens.size(); j > i; j--) {
                    List<String> curList = tokens.subList(i, j);
                    String curSubstring = String.join(SYMBOL_DELIM, curList);

                    if (isPriority(curSubstring) || isTag(curSubstring) ||
                            DateParser.isDate(curSubstring) ||
                            DateParser.isTime(curSubstring)) {
                        for (int k = 0; k < j - i; k++) {
                            tokens.remove(i);
                        }
                    }
                }
            }
            for (int i = 0; i < args.length(); i++) {
                String toRemove = "";
                for (int j = args.length(); j > i; j--) {
                    String curSubstring = args.substring(i, j);

                    if (isPriority(curSubstring) || isTag(curSubstring) ||
                            DateParser.isDate(curSubstring) ||
                            DateParser.isTime(curSubstring)) {
                        toRemove = curSubstring;
                    }
                }
                args = args.replace(toRemove, "");
            }
        }

        return args;
    }

    /**
     * Extracts the ignored segment (surrounded by SYMBOL_IGNORE) from a string.
     * @param args
     *      is the string to extract from.
     * @return
     *      null if no ignored segment exists, otherwise, the ignored segment.
     */
    private static String getIgnoredSegment(String args) {
        int startIdx = args.indexOf(SYMBOL_IGNORE);
        int endIdx = args.indexOf(SYMBOL_IGNORE, startIdx + 1);

        if (endIdx != -1) {
            String ignoredSegment = args.substring(startIdx, endIdx + 1);
            return ignoredSegment;
        }

        return null;
    }

    private static boolean hasIgnoredSegment(String args) {
        return getIgnoredSegment(args) != null;
    }

    public static String parseName(String args) {
        // TODO fix this horrible, horrible code
        StringBuilder sB = new StringBuilder(args);
        Queue<String> outside = new LinkedList<String>();
        Queue<String> inside = new LinkedList<String>();

        boolean hasIgnoreSegment;
        int curIdx = 0;
        do {
            int startIgnoreIdx = sB.indexOf(SYMBOL_IGNORE, curIdx);
            int endIgnoreIdx = startIgnoreIdx == -1 ? -1 :
                sB.indexOf(SYMBOL_IGNORE, startIgnoreIdx + 1);
            hasIgnoreSegment = endIgnoreIdx > startIgnoreIdx;

            if (!hasIgnoreSegment) {
                outside.offer(sB.substring(curIdx, sB.length()));
            } else {
                outside.offer(sB.substring(curIdx, startIgnoreIdx));
                inside.offer(sB.substring(startIgnoreIdx, endIgnoreIdx + 1));
            }
            curIdx = endIgnoreIdx + 1;
        } while (hasIgnoreSegment);

        for (int i = 0; i < outside.size(); i++) {
            String s = outside.poll();
            sB = new StringBuilder(s);
            for (int j = 0; j < sB.length(); j++) {
                for (int k = sB.length(); k > j; k--) {
                    String possibleDateTime = sB.substring(j, k);
                    LocalDate date = DateParser.parseDate(possibleDateTime);
                    LocalTime time = DateParser.parseTime(possibleDateTime);
                    if (date != null || time != null) {
                        sB.delete(j--, k);
                        break;
                    }
                }
            }
            outside.offer(sB.toString());
        }

        for (int i = 0; i < outside.size(); i++) {
            String s = outside.poll();
            s = stripTags(s);
            s = stripPriority(s);
            outside.offer(s);
        }

        sB = new StringBuilder();
        while (!outside.isEmpty()) {
            sB.append(outside.poll());
            if (!inside.isEmpty()) {
                sB.append(inside.poll());
            }
        }

        return sB.toString().trim();
        //return args;
        // TODO Proper parsing.
    }

    private static String stripTags(String args) {
        StringBuilder sB = new StringBuilder(args);

        boolean shouldCheckFurther;
        do {
            int startTagIdx = sB.indexOf(SYMBOL_TAG);
            int endTagIdx = startTagIdx == -1 ? -1 :
                sB.indexOf(SYMBOL_DELIM, startTagIdx + 1);

            shouldCheckFurther = endTagIdx > startTagIdx;
            if (shouldCheckFurther) {
                sB.delete(startTagIdx, endTagIdx + 1);
            } else if (startTagIdx != -1) {
                // last word in line
                sB.delete(startTagIdx, sB.length());
            }
        } while (shouldCheckFurther);

        return sB.toString();
    }

    private static String stripPriority(String args) {
        StringBuilder sB = new StringBuilder(args);

        boolean shouldCheckFurther;
        do {
            int startPriorityIdx = sB.indexOf(SYMBOL_PRIORITY);
            int endPriorityIdx = startPriorityIdx == -1 ? -1 :
                sB.indexOf(SYMBOL_DELIM, startPriorityIdx + 1);

            shouldCheckFurther = endPriorityIdx > startPriorityIdx;
            if (shouldCheckFurther) {
                sB.delete(startPriorityIdx, endPriorityIdx + 1);
            } else if (startPriorityIdx != -1) {
                // last word in line
                sB.delete(startPriorityIdx, sB.length());
            }
        } while (shouldCheckFurther);

        return sB.toString();
    }

    public static void parseDateTime(String args, TaskInfo task) {
        args = stripIgnoredSegments(args);
        DateParser.parseDateTime(args, task);
    }

    public static Tag[] parseTags(String args) {
        args = stripIgnoredSegments(args);
        args = cleanCmdString(args);
        String[] words = args.split(SYMBOL_DELIM);

        List<Tag> tagList = new ArrayList<Tag>();
        for (String word : words) {
            if (isTag(word)) {
                word = removeFirstChar(word);
                if (!word.isEmpty()) {
                    tagList.add(new Tag(word));
                }
            }
        }
        Tag[] tags = tagList.toArray(new Tag[tagList.size()]);

        return tags.length == 0 ? null : tags;
    }

    private static boolean isTag(String possibleTag) {
        return possibleTag.startsWith(SYMBOL_TAG) &&
                !possibleTag.contains(SYMBOL_DELIM);
    }

    public static Priority parsePriority(String args) {
        args = stripIgnoredSegments(args);
        args = cleanCmdString(args);
        String[] words = args.split(SYMBOL_DELIM);

        Priority p = DEFAULT_PRIORITY;

        for (String word : words) {
            p = matchPriority(word);

            // match only the first recognised priority
            if (p != DEFAULT_PRIORITY) {
                break;
            }
        }

        return p;
    }

    private static boolean isPriority(String possiblePriority) {
        boolean isPriority = matchPriority(possiblePriority) != null;
        return isPriority;
    }

    private static Priority matchPriority(String possiblePriority) {
        Priority p = DEFAULT_PRIORITY;

        if (possiblePriority.startsWith(SYMBOL_PRIORITY) &&
                !possiblePriority.contains(SYMBOL_DELIM)) {
            String type = removeFirstChar(possiblePriority);
            switch (type.toLowerCase()) {
                case "high" :
                    p = Priority.HIGH;
                    break;
                case "med" :
                    p = Priority.MEDIUM;
                    break;
                case "low" :
                    p = Priority.LOW;
                    break;
            }
        }

        return p;
    }

    private static String removeFirstChar(String s) {
        return s.substring(1);
    }

    private static String cleanCmdString(String cmdString) {
        return stripExtraDelims(cmdString).trim();
    }

    private static String stripExtraDelims(String s) {
        String doubleDelim = SYMBOL_DELIM + SYMBOL_DELIM;
        while (s.indexOf(doubleDelim) != -1) {
            s = s.replace(doubleDelim, SYMBOL_DELIM);
        }
        return s;
    }

    private static String stripIgnoredSegments(String s) {
        StringBuilder sB = new StringBuilder(s);

        boolean shouldCheckFurther;
        do {
            int startIgnoreIdx = sB.indexOf(SYMBOL_IGNORE);
            int endIgnoreIdx = sB.indexOf(SYMBOL_IGNORE, startIgnoreIdx + 1);

            shouldCheckFurther = endIgnoreIdx > startIgnoreIdx;
            if (shouldCheckFurther) {
                sB.delete(startIgnoreIdx, endIgnoreIdx + 1);
            }
        } while (shouldCheckFurther);

        return sB.toString();
    }

}
