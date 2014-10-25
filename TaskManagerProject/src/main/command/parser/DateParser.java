package main.command.parser;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import data.taskinfo.TaskInfo;

public class DateParser {
    private static final String SYMBOL_DELIM = " ";

    private static Map<DateTimeFormatter, String> datePartialFormatPatterns;
    private static Map<DateTimeFormatter, String> dateFullFormatPatterns;
    private static LocalDate datePatternsLastUpdate;
    private static Map<DateTimeFormatter, String> timeFullFormatPatterns;


    public static void parseDateTime(String dateTimeString, TaskInfo task) {
        while (dateTimeString.contains(SYMBOL_DELIM + SYMBOL_DELIM)) {
            dateTimeString = dateTimeString.replace(SYMBOL_DELIM + SYMBOL_DELIM, SYMBOL_DELIM);
        }
        // list for indices of the delimiter
        List<Integer> delimIndices = new ArrayList<Integer>();
        delimIndices.add(0);
        for (int i = dateTimeString.indexOf(SYMBOL_DELIM, 1);
                i > 0; i = dateTimeString.indexOf(SYMBOL_DELIM, i + 1)) {
            delimIndices.add(i + 1);
        }
        delimIndices.add(dateTimeString.length());

        List<LocalTime> times = new ArrayList<LocalTime>();
        List<LocalDate> dates = new ArrayList<LocalDate>();

        // try to parse times and dates from every possible sequence of words
        for (int offset = delimIndices.size() - 1; offset > 0; offset--) {
            for (int i = 0; !delimIndices.isEmpty() && i + offset < delimIndices.size(); i++) {
                // if more than two times and dates, take the first two of each
                if (times.size() > 1 && dates.size() > 1) {
                    break;
                }

                int frontIdx = delimIndices.get(i);
                int backIdx = delimIndices.get(i + offset);
                String wordSeq = dateTimeString.substring(frontIdx, backIdx)
                                                   .trim();

                LocalTime t = parseTime(wordSeq);
                LocalDate d = parseDate(wordSeq);

                // remove sequence from String if it's parsed into a date or time
                if (t != null || d != null) {
                    dateTimeString = dateTimeString.substring(0, frontIdx) +
                            dateTimeString.substring(backIdx, dateTimeString.length());

                    // amend the delimIndices list for the new String
                    int delimSize = delimIndices.size();
                    int delimOffset = backIdx - frontIdx;
                    for (int j = i; j + offset < delimSize; j++) {
                        delimIndices.set(j, delimIndices.get(j + offset) - delimOffset);
                    }
                    for (int j = 0; j < offset; j++) {
                        delimIndices.remove(delimIndices.size() - 1);
                    }
                    // don't move the iterator as we're deleting the current one
                    --i;
                }
                // and store it into the list
                if (t != null) {
                    times.add(t);
                }
                if (d != null) {
                    dates.add(d);
                }
            }
        }

        // at least one time for each date, stop if otherwise
        if (times.size() < dates.size()) {
            return;
        }

        LocalDateTime dateTimeStart = null;
        Duration dateTimeDiff = Duration.ZERO;

        LocalTime timeA = null;
        LocalTime timeB = null;

        if (times.size() > 0) {
            timeA = times.get(0);

            // case: no specified dates
            if (dates.isEmpty()) {
                if (LocalTime.now().isAfter(timeA)) {
                    dateTimeStart = timeA.atDate(LocalDate.now().plusDays(1));
                } else {
                    dateTimeStart = timeA.atDate(LocalDate.now());
                }
            } else {
                dateTimeStart = LocalDateTime.of(dates.get(0), timeA);
            }
        }

        if (times.size() > 1) {
            timeB = times.get(1);
            dateTimeDiff = Duration.between(timeA, timeB);

            // case: no specified dates
            if (dates.size() <= 1) {
                if (timeA.isAfter(timeB)) {
                    dateTimeDiff = dateTimeDiff.plusDays(1);
                }
            } else {
                dateTimeDiff = dateTimeDiff
                        .plusDays(dates.get(0).until(dates.get(1), ChronoUnit.DAYS));
            }
        }

        // check if there are any datetimes
        if (dateTimeStart != null) {
            task.endDate = dateTimeStart.plus(dateTimeDiff).toLocalDate();
            task.endTime = dateTimeStart.plus(dateTimeDiff).toLocalTime();

            if (!dateTimeDiff.isZero()) {
                task.startDate = dateTimeStart.toLocalDate();
                task.startTime = dateTimeStart.toLocalTime();
            }
        }
    }


    public static DateTimePair parseDateTimesInSequence(String dateTimeString) {
        return parseDateTimes(dateTimeString, true);
    }

    public static DateTimePair parseDateTimes(String dateTimeString) {
        return parseDateTimes(dateTimeString, false);
    }

    private static DateTimePair parseDateTimes(
            String dateTimeString, boolean isInSequence) {
        String[] tokens = dateTimeString.split(SYMBOL_DELIM);
        DateTimePair dtPair = isInSequence ?
                new DateTimeSequence() : new DateTimePair();

        for (int i = 0; i < tokens.length; i++) {
            for (int j = tokens.length; j > i; j--) {
                if (dtPair.isFull()) {
                    break;
                }

                String[] curTokens = Arrays.copyOfRange(tokens, i, j);
                String curSubstring = String.join(SYMBOL_DELIM, curTokens);

                LocalDate d = parseDate(curSubstring);
                LocalTime t = parseTime(curSubstring);

                dtPair.add(d);
                dtPair.add(t);

                if (d != null || t != null) {
                    i = j - 1;
                    break;
                }
            }
        }

        return dtPair;
    }

    public static LocalDate parseDate(String dateString) {
        buildDatePatternHashMap();

        LocalDate d = parseRelativeDate(dateString);
        if (d == null) {
            d = parseAbsoluteDate(dateString);
        }

        return d;
    }

    public static boolean isDate(String dateString) {
        return parseDate(dateString) != null;
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
        // change String to titlecase (e.g. sep -> Sep; parse is case sensitive)
        dateString = capitaliseFirstLetter(dateString);

        // match full before partial in order to prevent matching more than one.
        LocalDate date = matchDatePatterns(dateFullFormatPatterns, dateString);
        if (date == null) {
            date = matchDatePatterns(datePartialFormatPatterns, dateString);
        }

        return date;
    }

    private static LocalDate matchDatePatterns(
            Map<DateTimeFormatter, String> dateMap, String dateString) {

        Iterator<Entry<DateTimeFormatter, String>> i =
            dateMap.entrySet().iterator();
        while (i.hasNext()) {
            Entry<DateTimeFormatter, String> formatPattern = i.next();

            DateTimeFormatter format = formatPattern.getKey();
            String missingField = formatPattern.getValue();

            try {
                String test = dateString + missingField;
                LocalDate d = LocalDate.parse(test, format);
                return d;
            } catch (DateTimeParseException e) {
                // do nothing
            }
        }

        return null;
    }

    private static void buildDatePatternHashMap() {
        // last update was on the same date
        if (datePatternsLastUpdate != null &&
                datePatternsLastUpdate.equals(LocalDate.now())) {
            return;
        }

        datePartialFormatPatterns = new HashMap<DateTimeFormatter, String>();

        // 24 August
        mapPattern(datePartialFormatPatterns,
                "d MMMM" + "y", String.valueOf(LocalDate.now().getYear()));
        // 24 Aug
        mapPattern(datePartialFormatPatterns,
                "d MMM" + "y", String.valueOf(LocalDate.now().getYear()));
        // 24Aug
        mapPattern(datePartialFormatPatterns,
                "dMMM" + "y", String.valueOf(LocalDate.now().getYear()));
        // 24/8, 24/08, 04/08, 04/8
        mapPattern(datePartialFormatPatterns,
                "d/M" + "/y", "/" + String.valueOf(LocalDateTime.now().getYear()));

        datePatternsLastUpdate = LocalDate.now();

        // partial / full divider, full only needs to be built once
        if (dateFullFormatPatterns != null) {
            return;
        }

        dateFullFormatPatterns = new HashMap<DateTimeFormatter, String>();

        // 24 Aug 2014
        mapPattern(dateFullFormatPatterns, "d MMM y");
        // 24Aug2014
        mapPattern(dateFullFormatPatterns, "dMMMy");
        // 24 Aug 14
        mapPattern(dateFullFormatPatterns, "d MMM yy");
        // 24Aug14
        mapPattern(dateFullFormatPatterns, "dMMMyy");
        // 24 August 2014
        mapPattern(dateFullFormatPatterns, "d MMMM y");
        // 24/8/14, 24/08/14, 04/08/14, 04/8/14
        mapPattern(dateFullFormatPatterns, "d/M/yy");
    }

    public static LocalTime parseTime(String timeString) {
        buildTimePatternHashMap();

        LocalTime t = parseRelativeTime(timeString);
        if (t == null) {
            t = parseAbsoluteTime(timeString);
        }

        return t;
    }

    public static boolean isTime(String timeString) {
        return parseTime(timeString) != null;
    }

    private static LocalTime parseRelativeTime(String timeString) {
        // TODO Support for relative times (+3h, -3h, now, etc)
        return null;
    }

    private static LocalTime parseAbsoluteTime(String timeString) {
        // 3 pm -> 3 PM
        timeString = timeString.toUpperCase();
        LocalTime time = matchTimePatterns(timeFullFormatPatterns, timeString);
        return time;
    }

    private static LocalTime matchTimePatterns(
            Map<DateTimeFormatter, String> timeMap, String timeString) {

        Iterator<Entry<DateTimeFormatter, String>> i =
                timeMap.entrySet().iterator();
            while (i.hasNext()) {
                Entry<DateTimeFormatter, String> formatPattern = i.next();

                DateTimeFormatter format = formatPattern.getKey();
                String missingField = formatPattern.getValue();

                try {
                    return LocalTime.parse(timeString + missingField, format);
                } catch (DateTimeParseException e) {
                    // do nothing
                }
            }

        return null;
    }

    private static void buildTimePatternHashMap() {
        if (timeFullFormatPatterns != null) {
            return;
        }
        timeFullFormatPatterns = new HashMap<DateTimeFormatter, String>();

        // 15:46
        mapPattern(timeFullFormatPatterns, "H:m");
        // 3:46 PM
        mapPattern(timeFullFormatPatterns, "h:m a");
        // 3:46PM
        mapPattern(timeFullFormatPatterns, "h:m a");
        // 3 PM
        mapPattern(timeFullFormatPatterns, "h a");
        // 3PM
        mapPattern(timeFullFormatPatterns, "ha");
    }

    private static void mapPattern(Map<DateTimeFormatter, String> map,
            String pattern) {
        map.put(DateTimeFormatter.ofPattern(pattern), "");
    }

    private static void mapPattern(Map<DateTimeFormatter, String> map,
            String pattern, String missingField) {
        map.put(DateTimeFormatter.ofPattern(pattern), missingField);
    }

    private static String capitaliseFirstLetter(String dateString) {
        dateString = dateString.toUpperCase();
        for (int i = 0; i < dateString.length(); i++) {
            if (Character.isAlphabetic(dateString.charAt(i))) {
                dateString = dateString.substring(0, i + 1) +
                        dateString.substring(i + 1).toLowerCase();
                break;
            }
        }
        return dateString;
    }
}
