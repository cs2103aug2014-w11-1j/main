package main.command.parser;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

//@author A0111862M
/**
 * Utility class for parsing dates and times from strings, uses DateParser and
 * TimeParser for the actual date and time parsing.
 */
public class DateTimeParser {
    // enum for modifying dates when applicable (e.g. last/this/next christmas)
    enum DateModifier {
        THIS, NEXT, PREVIOUS
    }

    private static final String SYMBOL_DELIM = " ";
    private static Set<String> prepositions;
    private static HashMap<String, DateModifier> modifierMap;

    /**
     * Parses dates and times from {@code args} into a DateTimePair.
     * This method parses the dates and times in sequence, so
     * "5 Nov to 6 Nov 3pm" will be parsed with 3pm in the second time instead
     * of the first.
     *
     * @param dateTimeString
     *            the string possibly containing dates and times
     * @return a DateTimePair representing the dates and times found
     */
    public static DateTimePair parseDateTimesInSequence(String dateTimeString) {
        return parseDateTimes(dateTimeString, true);
    }

    /**
     * Parses dates and times from {@code args} into a DateTimePair.
     * This method does not parses the dates and times in sequence, so
     * "5 Nov to 6 Nov 3pm" will be parsed with 3pm in the first time instead of
     * the second.
     *
     * @param dateTimeString
     *            the string possibly containing dates and times
     * @return a DateTimePair representing the dates and times found
     */
    public static DateTimePair parseDateTimes(String dateTimeString) {
        return parseDateTimes(dateTimeString, false);
    }

    /**
     * Parses dates and times from {@code dateTimeString} into a DateTimePair.
     * This method parses the dates and times in sequence according to the value
     * of the {@code isInSequence} flag.
     *
     * @param dateTimeString
     *            the string possibly containing dates and times
     * @param isInSequence
     *            whether to parse in sequence or not
     * @return a DateTimePair representing the dates and times found
     */
    private static DateTimePair parseDateTimes(
            String dateTimeString, boolean isInSequence) {

        String[] tokens = dateTimeString.split(SYMBOL_DELIM);
        DateTimePair dtPair = isInSequence ?
                new DateTimeSequence() : new DateTimePair();

        // substrings starting from the largest possible to the smallest
        // possible to ensure the full date / time is recognised.
        for (int fromIdx = 0; fromIdx < tokens.length; fromIdx++) {
            for (int toIdx = tokens.length; toIdx > fromIdx; toIdx--) {
                if (dtPair.isFull()) {
                    break;
                }

                String[] curTokens = Arrays.copyOfRange(tokens, fromIdx, toIdx);
                String curSubstring = String.join(SYMBOL_DELIM, curTokens);

                curSubstring = removePrepositions(curSubstring);
                DateModifier modifier = getDateModifier(curSubstring);
                if (modifier != null) {
                    curSubstring = removeFirstWord(curSubstring);
                }

                ParsedDate d = parseDate(curSubstring);
                LocalTime t = parseTime(curSubstring);

                boolean hasFound = d != null || t != null;
                if (d != null) {
                    dtPair.add(d);
                }
                if (t != null) {
                    dtPair.add(t);
                }

                if (hasFound) {
                    if (modifier != null) {
                        dtPair.add(modifier);
                    }

                    // skip ahead by the number of tokens in the date/time found
                    fromIdx = toIdx - 1;
                    break;
                }
            }
        }

        return dtPair;
    }

    private static ParsedDate parseDate(String dateString) {
        return DateParser.parseDate(dateString);
    }

    private static LocalTime parseTime(String timeString) {
        return TimeParser.parseTime(timeString);
    }

    public static boolean isDate(String dateString) {
        dateString = cleanDateTimeString(dateString);
        return DateParser.isDate(dateString);
    }

    public static boolean isTime(String timeString) {
        timeString = cleanDateTimeString(timeString);
        return TimeParser.isTime(timeString);
    }

    /**
     * Removes possible prepositions and a date modifier from
     * {@code dateTimeString}.
     *
     * @param dateTimeString a string to be parsed
     * @return the string with prepositions and date modifier removed
     */
    private static String cleanDateTimeString(String dateTimeString) {
        dateTimeString = removePrepositions(dateTimeString);
        if (getDateModifier(dateTimeString) != null) {
            dateTimeString = removeFirstWord(dateTimeString);
        }
        return dateTimeString;
    }

    /**
     * Gets a date modifier that corresponds to the string passed in.
     *
     * @param possibleDateModifier
     *            a possible date modifier
     * @return the date modifier if it exists
     */
    private static DateModifier getDateModifier(String possibleDateModifier) {
        buildModifierMap();

        String possibleModifier = possibleDateModifier.split(SYMBOL_DELIM)[0];
        return modifierMap.get(possibleModifier.toLowerCase());
    }

    private static void buildModifierMap() {
        if (modifierMap != null) {
            return;
        }

        modifierMap = new HashMap<>();

        modifierMap.put("this", DateModifier.THIS);
        modifierMap.put("next", DateModifier.NEXT);
        modifierMap.put("previous", DateModifier.PREVIOUS);
        modifierMap.put("last", DateModifier.PREVIOUS);
    }

    /**
     * Removes the first word (token) from a string.
     *
     * @param string the string to remove the first word from
     * @return the string with the first word removed or the string if empty
     */
    private static String removeFirstWord(String string) {
        assert string != null;

        if (!string.isEmpty()) {
            String[] split = string.split(SYMBOL_DELIM, 2);
            return split.length > 1 ? split[1] : split[0];
        } else {
            return string;
        }
    }

    /**
     * Removes prepositions from the front of a string.
     *
     * @param phrase the phrase to be parsed
     * @return the phrase with any prepositions in front removed
     */
    private static String removePrepositions(String phrase) {
        buildPrepositionSet();

        int lastFoundPreposition = 0;
        String[] tokens = phrase.toLowerCase().split(SYMBOL_DELIM);

        // find last preposition (e.g. "at around 3pm" -> index 1, "around")
        while (tokens.length > lastFoundPreposition &&
                prepositions.contains(tokens[lastFoundPreposition])) {
            ++lastFoundPreposition;
        }

        if (lastFoundPreposition == 0) {
            return phrase;
        } else {
            tokens = Arrays.copyOfRange(
                    tokens, lastFoundPreposition, tokens.length);
            return String.join(SYMBOL_DELIM, tokens).trim();
        }
    }

    private static void buildPrepositionSet() {
        if (prepositions != null) {
            return;
        }

        String[] prepositionArr = {"about", "after", "around", "at", "before",
                "between", "by", "for", "from", "in", "near", "on", "past",
                "round", "since", "till", "to", "until", "within"};
        prepositions = new HashSet<>(Arrays.asList(prepositionArr));
    }

}
