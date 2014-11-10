package main.command.parser;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import main.command.parser.ParsedDate.Frequency;

//@author A0111862M
/**
 * Utility class for parsing dates from strings.
 */
public class DateParser {
    private static final int MIN_YEAR = 1000;
    private static Map<DateTimeFormatter, String> datePartialFormatPatterns;
    private static Map<DateTimeFormatter, String> dateFullFormatPatterns;
    private static Map<String, String> dateWeekOfDayShortForms;
    private static LocalDate datePatternsLastUpdate;

    /**
     * Parses a string to a date.
     *
     * @param dateString
     *            a string that is possibly a date
     * @return the LocalDate corresponding to the string
     */
    static ParsedDate parseDate(String dateString) {
        buildDatePatternHashMap();

        ParsedDate d = parseRelativeDate(dateString);
        if (d == null) {
            d = parseAbsoluteDate(dateString);
        }

        return d;
    }

    /**
     * Checks if a string can be parsed into a date
     *
     * @param dateString
     *            a string that is possibly a date
     * @return true if the string can be parsed into a date, false otherwise
     */
    static boolean isDate(String dateString) {
        return parseDate(dateString) != null;
    }

    /**
     * Parses strings in a format relative to the current date.
     */
    private static ParsedDate parseRelativeDate(String dateString) {
        LocalDate parsedDate = null;
        dateString = dateString.toLowerCase();

        parsedDate = parseDateAsDayOfWeek(dateString);
        if (parsedDate != null) {
            return new ParsedDate(parsedDate, Frequency.WEEK);
        }

        parsedDate = parseDateAsOccasion(dateString);
        if (parsedDate != null) {
            return new ParsedDate(parsedDate, Frequency.YEAR);
        }

        parsedDate = parseDateAsPlusMinus(dateString);
        if (parsedDate == null) {
            parsedDate = parseDateAsRelativeToNow(dateString);
        }
        if (parsedDate != null) {
            return new ParsedDate(parsedDate);
        }

        return null;
    }

    /**
     * Parses strings in English relative to the current date, like "today",
     * "yesterday", or "tomorrow".
     *
     * @param dateString
     *            a possible relative date
     * @return the date if valid, null otherwise
     */
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

    /**
     * Parses strings of annual occasions such as Christmas to their respective
     * dates.
     *
     * @param dateString
     *            a possible occasion
     * @return the date if valid, null otherwise
     */
    private static LocalDate parseDateAsOccasion(String dateString) {
        if (dateString.toLowerCase().equals("christmas")) {
            return LocalDate.of(LocalDate.now().getYear(), 12, 25);
        }
        // TODO Occasions such as New Year's / Christmas.
        return null;
    }

    /**
     * Parses a string as a day of the week (e.g. monday, tuesday, etc).
     *
     * @param dateString
     *            a string that is possibly a day of the week
     * @return the closest date to today with that day of the week if the string
     *         is valid, or null otherwise
     */
    private static LocalDate parseDateAsDayOfWeek(String dateString) {
        buildWeekOfDayMap();

        if (dateWeekOfDayShortForms.containsKey(dateString.toLowerCase())) {
            dateString = dateWeekOfDayShortForms.get(dateString);
        }
        try {
            dateString = dateString.toUpperCase();
            int dayOfWeek = DayOfWeek.valueOf(dateString).getValue();

            return LocalDate.now().with(ChronoField.DAY_OF_WEEK, dayOfWeek);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static void buildWeekOfDayMap() {
        if (dateWeekOfDayShortForms != null) {
            return;
        }

        dateWeekOfDayShortForms = new HashMap<>();
        dateWeekOfDayShortForms.put("mon", "monday");
        dateWeekOfDayShortForms.put("tue", "tuesday");
        dateWeekOfDayShortForms.put("wed", "wednesday");
        dateWeekOfDayShortForms.put("thu", "thursday");
        dateWeekOfDayShortForms.put("fri", "friday");
        dateWeekOfDayShortForms.put("sat", "saturday");
        dateWeekOfDayShortForms.put("sun", "sunday");
    }

    /**
     * Parses string in the format: "+xd", or its minus variant. This refers to
     * x number of days relative to the current date.
     *
     * @param dateString
     *            a possible relative date
     * @return the date if valid, null otherwise
     */
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

    private static ParsedDate parseAbsoluteDate(String dateString) {
        // match full before partial in order to prevent matching more than one.
        LocalDate date = matchDatePatterns(dateFullFormatPatterns, dateString);
        if (date != null) {
            return new ParsedDate(date);
        }

        date = matchDatePatterns(datePartialFormatPatterns, dateString);
        if (date != null) {
            return new ParsedDate(date, Frequency.YEAR);
        }

        return null;
    }

    /**
     * Matches a string to a map of date formats.
     *
     * @param dateMap
     *            the map of date formats
     * @param dateString
     *            the string to be matched
     * @return the LocalDate corresponding to the string if valid, null
     *         otherwise
     */
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

    private static void buildDatePatternHashMap() {
        // last update was on the same date
        if (datePatternsLastUpdate != null &&
                datePatternsLastUpdate.equals(LocalDate.now())) {
            return;
        }

        datePartialFormatPatterns = new HashMap<>();

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

        dateFullFormatPatterns = new HashMap<>();

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

    /**
     * Capitalises the first letter (alphabetic) of the string and returns it.
     * (e.g. "12%ab" -> "12%Ab")
     */
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
