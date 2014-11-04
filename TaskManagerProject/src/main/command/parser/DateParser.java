package main.command.parser;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class DateParser {
    private static final int MIN_YEAR = 1000;
    private static Map<DateTimeFormatter, String> datePartialFormatPatterns;
    private static Map<DateTimeFormatter, String> dateFullFormatPatterns;
    private static Map<String, String> dateWeekOfDayShortForms;
    private static LocalDate datePatternsLastUpdate;

    static LocalDate parseDate(String dateString) {
        buildDatePatternHashMap();

        LocalDate d = parseRelativeDate(dateString);
        if (d == null) {
            d = parseAbsoluteDate(dateString);
        }

        return d;
    }

    static boolean isDate(String dateString) {
        return parseDate(dateString) != null;
    }

    private static LocalDate parseRelativeDate(String dateString) {
        // TODO weekdays (absolute?), +7d, -7d
        LocalDate parsedDate = null;
        dateString = dateString.toLowerCase();

        parsedDate = parseDateAsDayOfWeek(dateString);
        if (parsedDate != null) {
            return parsedDate;
        }

        parsedDate = parseDateAsOccasion(dateString);
        if (parsedDate != null) {
            return parsedDate;
        }

        parsedDate = parseDateAsPlusMinus(dateString);
        if (parsedDate == null) {
            parsedDate = parseDateAsRelativeToNow(dateString);
        }
        return parsedDate;
    }

    private static LocalDate parseDateAsRelativeToNow(String dateString) {
        LocalDate parsedDate = null;

        switch (dateString) {
            case "yesterday" :
                parsedDate = LocalDate.now().minusDays(1);
                break;
            case "today" :
            case "now" :
                parsedDate = LocalDate.now();
                break;
            case "tomorrow" :
                parsedDate = LocalDate.now().plusDays(1);
                break;
        }

        return parsedDate;
    }

    private static LocalDate parseDateAsOccasion(String dateString) {
        // TODO Auto-generated method stub
        return null;
    }

    private static LocalDate parseDateAsDayOfWeek(String dateString) {
        switch (dateString) {
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
                return getNextDayOfWeek(dateString);
        }

        return null;
    }

    private static LocalDate parseDateAsPlusMinus(String dateString) {
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
        LocalDate parsedDate;

        // match full before partial in order to prevent matching more than one.
        LocalDate date = matchDatePatterns(dateFullFormatPatterns, dateString);
        if (date != null) {
            parsedDate = date;
        } else {
            date = matchDatePatterns(datePartialFormatPatterns, dateString);
            parsedDate = date;
        }

        return parsedDate;
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
        // 24/8/2014, 24/08/2014, 04/08/2014, 04/8/2014
        mapPattern(dateFullFormatPatterns, "d/M/y");
        // 24/8/14, 24/08/14, 04/08/14, 04/8/14
        mapPattern(dateFullFormatPatterns, "d/M/yy");
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
