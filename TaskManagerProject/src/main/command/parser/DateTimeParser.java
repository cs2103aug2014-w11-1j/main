package main.command.parser;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class DateTimeParser {
    private static final String SYMBOL_DELIM = " ";
    private static final int MIN_YEAR = 1000;

    private static Map<DateTimeFormatter, String> datePartialFormatPatterns;
    private static Map<DateTimeFormatter, String> dateFullFormatPatterns;
    private static Map<String, String> dateWeekOfDayShortForms;
    private static LocalDate datePatternsLastUpdate;
    private static Map<DateTimeFormatter, String> timeFullFormatPatterns;
    private static Set<String> prepositions;

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

        dateString = removePrepositions(dateString);

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
        LocalDate parsedDate = null;
        switch (dateString.toLowerCase()) {
            case "mon" :
            case "monday" :
            case "tue" :
            case "tuesday" :
            case "wed" :
            case "wednesday" :
            case "thu" :
            case "thursday" :
            case "fri" :
            case "friday" :
            case "sat" :
            case "saturday" :
            case "sun" :
            case "sunday" :
                parsedDate = getNextDayOfWeek(dateString);
            case "yesterday" :
                parsedDate = LocalDate.now().minusDays(1);
            case "today" :
            case "now" :
                parsedDate = LocalDate.now();
            case "tomorrow" :
                parsedDate = LocalDate.now().plusDays(1);
        }
        parsedDate = parseRelativeDateAsPlusMinus(dateString);

        return parsedDate;
    }

    private static LocalDate parseRelativeDateAsPlusMinus(String dateString) {
        String modifier = null;
        if ((dateString.startsWith("+") || dateString.startsWith("-")) &&
                dateString.endsWith("d")) {
            modifier = dateString.substring(0, dateString.length() - 1);
        }
        try {
            int modifierInt = Integer.parseInt(modifier);
            return LocalDate.now().plusDays(modifierInt);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static LocalDate getNextDayOfWeek(String dateString) {
        buildWeekOfDayMap();

        dateString = dateString.toLowerCase();
        if (dateWeekOfDayShortForms.containsKey(dateString)) {
            dateString = dateWeekOfDayShortForms.get(dateString);
        }
        DayOfWeek dayOfWeek = DayOfWeek.valueOf(dateString.toUpperCase());
        Temporal adjustedDay = TemporalAdjusters.next(dayOfWeek)
                .adjustInto(LocalDate.now());
        return LocalDate.from(adjustedDay);
    }

    private static LocalDate parseAbsoluteDate(String dateString) {
        // match full before partial in order to prevent matching more than one.
        LocalDate date = matchDatePatterns(dateFullFormatPatterns, dateString);
        if (date == null) {
            date = matchDatePatterns(datePartialFormatPatterns, dateString);
        }

        return date;
    }

    private static LocalDate matchDatePatterns(
            Map<DateTimeFormatter, String> dateMap, String dateString) {
        // change String to titlecase (e.g. sep -> Sep; parse is case sensitive)
        dateString = capitaliseFirstLetter(dateString);

        Iterator<Entry<DateTimeFormatter, String>> i =
            dateMap.entrySet().iterator();
        while (i.hasNext()) {
            Entry<DateTimeFormatter, String> formatPattern = i.next();

            DateTimeFormatter format = formatPattern.getKey();
            String missingField = formatPattern.getValue();

            try {
                String test = dateString + missingField;
                LocalDate d = LocalDate.parse(test, format);
                if (d.getYear() > MIN_YEAR) {
                    return d;
                }
            } catch (DateTimeParseException e) {
                // do nothing
            }
        }

        return null;
    }

    private static void buildWeekOfDayMap() {
        if (dateWeekOfDayShortForms != null) {
            return;
        }

        dateWeekOfDayShortForms = new HashMap<String, String>();
        dateWeekOfDayShortForms.put("mon", "monday");
        dateWeekOfDayShortForms.put("tue", "tuesday");
        dateWeekOfDayShortForms.put("wed", "wednesday");
        dateWeekOfDayShortForms.put("thu", "thursday");
        dateWeekOfDayShortForms.put("fri", "friday");
        dateWeekOfDayShortForms.put("sat", "saturday");
        dateWeekOfDayShortForms.put("sun", "sunday");
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

        timeString = removePrepositions(timeString);

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
        LocalTime parsedTime = null;
        parsedTime = parseRelativeTimeAsPlusMinus(timeString);
        return parsedTime;
    }

    private static LocalTime parseRelativeTimeAsPlusMinus(String timeString) {
        // TODO refactor into enum map to support "second", "sec", "min", etc.
        timeString = timeString.toLowerCase();
        String number = null;
        String unit = null;
        if ((timeString.startsWith("+") || timeString.startsWith("-")) &&
                (timeString.endsWith("s") || timeString.endsWith("m") ||
                        timeString.endsWith("h"))) {
            number = timeString.substring(0, timeString.length() - 1);
            unit = timeString.substring(timeString.length() - 1);
        }
        try {
            int modifierInt = Integer.parseInt(number);
            LocalTime now = LocalTime.now();
            switch (unit) {
                case "s" :
                    return now.plusSeconds(modifierInt);
                case "m" :
                    return now.plusMinutes(modifierInt);
                case "h" :
                    return now.plusHours(modifierInt);
                default :
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        } catch (NullPointerException e) {
            return null;
        }
    }

    private static LocalTime parseAbsoluteTime(String timeString) {
        LocalTime time = matchTimePatterns(timeFullFormatPatterns, timeString);
        return time;
    }

    private static LocalTime matchTimePatterns(
            Map<DateTimeFormatter, String> timeMap, String timeString) {
        // 3 pm -> 3 PM
        timeString = timeString.toUpperCase();

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

    private static String removePrepositions(String string) {
        buildPrepositionsSet();

        int lastFoundPreposition = 0;
        String[] tokens = string.toLowerCase().split(SYMBOL_DELIM);

        while (tokens.length > lastFoundPreposition &&
                prepositions.contains(tokens[lastFoundPreposition])) {
            ++lastFoundPreposition;
        }

        if (lastFoundPreposition == 0) {
            return string;
        } else {
            String[] curTokens = Arrays.copyOfRange(
                    tokens, lastFoundPreposition, tokens.length);
            return String.join(SYMBOL_DELIM, curTokens).trim();
        }
    }

    private static void buildPrepositionsSet() {
        if (prepositions != null) {
            return;
        }

        String[] preArr = {"about", "after", "around", "at", "before",
                "between", "by", "for", "from", "in", "near", "on", "past",
                "round", "since", "till", "to", "until", "within"};
        prepositions = new HashSet<String>(Arrays.asList(preArr));
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
