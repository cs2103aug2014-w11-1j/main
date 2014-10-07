package main.command;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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

        // date off for when no date specified + the first time will occur tmr.
        int dateOffset = 0;

        if (times.size() > 0) {
            timeA = times.get(0);

            // case: no specified dates
            if (dates.isEmpty()) {
                if (LocalTime.now().isAfter(timeA)) {
                    ++dateOffset;
                }
                dateTimeStart = timeA.atDate(LocalDate.now());
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
            task.endDate = dateTimeStart.plus(dateTimeDiff).plusDays(dateOffset)
                    .toLocalDate();
            task.endTime = dateTimeStart.plus(dateTimeDiff).plusDays(dateOffset)
                    .toLocalTime();
            task.duration = dateTimeDiff;
        }

    }

    // TODO currently specialised for search, refactor into search command?
    public static List<LocalDateTime> parseDateTime(String dateTimeString) {
        int curIndex = 0;
        List<LocalDate> dates = new ArrayList<LocalDate>();
        List<LocalTime> times = new ArrayList<LocalTime>();
        for (int i = 0; i < 2; i++) {
            dates.add(null);
            times.add(null);
        }

        LocalDate prevDate = null;
        LocalTime prevTime = null;
        for (int i = 0; i < dateTimeString.length(); i++) {
            for (int j = dateTimeString.length(); j > i; j--) {
                if (curIndex >= 2) {
                    break;
                }

                String possibleDateTime = dateTimeString.substring(i, j);
                LocalDate curDate = parseDate(possibleDateTime);
                LocalTime curTime = parseTime(possibleDateTime);

                // if nothing's found, continue; else move i-th index forward
                if (curDate == null && curTime == null) {
                    continue;
                } else {
                    i += possibleDateTime.length();
                }

                if (curDate != null) {
                    if (prevDate != null) {
                        ++curIndex;
                    }
                    dates.set(curIndex, curDate);
                    if (prevTime != null) {
                        ++curIndex;
                        prevTime = null;
                    } else {
                        prevDate = curDate;
                    }
                }
                if (curTime != null) {
                    if (prevTime != null) {
                        ++curIndex;
                    }
                    times.set(curIndex, curTime);
                    if (prevDate != null) {
                        ++curIndex;
                        prevDate = null;
                    } else {
                        prevTime = curTime;
                    }
                }
            }
        }

        // if no dates and times found, return null
        if (dates.get(0) == null && dates.get(1) == null &&
                times.get(0) == null && times.get(1) == null) {
            return null;
        }

        // fill in missing data with magic circuit powar

        // fill in start date
        if (dates.get(0) == null) {
            if (dates.get(1) != null) {
                dates.set(0, dates.get(1));
            } else {
                dates.set(0, LocalDate.now());
            }
        }

        // fill in end time
        if (times.get(1) == null) {
            if (times.get(0) != null && dates.get(1) == null) {
                times.set(1, times.get(0));
            } else {
                times.set(1, LocalTime.MAX);
            }
        }

        // fill in end date
        if (dates.get(1) == null) {
            dates.set(1, dates.get(0));
        }

        // fill in start time
        if (times.get(0) == null) {
            times.set(0, LocalTime.MIN);
        }

        List<LocalDateTime> dateTimes = new ArrayList<LocalDateTime>();
        dateTimes.add(LocalDateTime.of(dates.get(0), times.get(0)));
        dateTimes.add(LocalDateTime.of(dates.get(1), times.get(1)));

        return dateTimes;
    }

    public static LocalDate parseDate(String dateString) {
        buildDatePatternHashMap();

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
        // 24Aug
        mapPattern(datePartialFormatPatterns,
                "dMMM" + "y", String.valueOf(LocalDate.now().getYear()));

        datePatternsLastUpdate = LocalDate.now();

        // partial / full divider, full only needs to be built once
        if (dateFullFormatPatterns != null) {
            return;
        }

        dateFullFormatPatterns = new HashMap<DateTimeFormatter, String>();

        // 24Aug2014
        mapPattern(dateFullFormatPatterns, "dMMMy");
        // 24Aug14
        mapPattern(dateFullFormatPatterns, "dMMMyy");
        // 24/8/14
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
        // 3 PM
        mapPattern(timeFullFormatPatterns, "h a");
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
