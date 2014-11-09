package main.command.parser;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

//@author A0111862M
/**
 * Utility class for parsing times from strings.
 */
public class TimeParser {
    private static Map<DateTimeFormatter, String> timeFullFormatPatterns;

    /**
     * Parses a string to a time.
     *
     * @param timeString
     *            a string that is possibly a time
     * @return the LocalTime corresponding to the string
     */
    static LocalTime parseTime(String timeString) {
        buildTimePatternHashMap();

        LocalTime t = parseRelativeTime(timeString);
        if (t == null) {
            t = parseAbsoluteTime(timeString);
        }

        return t;
    }

    /**
     * Checks if a string can be parsed into a time
     *
     * @param timeString
     *            a string that is possibly a time
     * @return true if the string can be parsed into a time, false otherwise
     */
    static boolean isTime(String timeString) {
        return parseTime(timeString) != null;
    }

    /**
     * Parses strings in a format relative to the current time.
     */
    private static LocalTime parseRelativeTime(String timeString) {
        if (timeString.equalsIgnoreCase("now")) {
            return LocalTime.now();
        } else {
            return parseRelativeTimeAsPlusMinus(timeString);
        }
    }

    /**
     * Parses string in the formats: "+xh", "+xm", or "+xs", or their minus
     * variants. These refer to x number of hours, minutes, or seconds relative
     * to the current time.
     *
     * @param timeString
     *            a possible relative time
     * @return the time if valid, null otherwise
     */
    private static LocalTime parseRelativeTimeAsPlusMinus(String timeString) {
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

    /**
     * Matches a string to a map of time formats.
     *
     * @param timeMap
     *            the map of time formats
     * @param timeString
     *            the string to be matched
     * @return the LocalTime corresponding to the string if valid, null
     *         otherwise
     */
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
}
