package main.command.parser;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class DateTimeParser {
    enum DateModifier {
        THIS, NEXT, PREVIOUS
    }

    private static final String SYMBOL_DELIM = " ";
    private static Set<String> prepositions;
    private static HashMap<String, DateModifier> modifierMap;

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
                curSubstring = removePrepositions(curSubstring);
                DateModifier modifier = getDateModifier(curSubstring);
                if (modifier != null) {
                    curSubstring = removeFirstWord(curSubstring);
                }

                ParsedDate d = parseDate(curSubstring);
                LocalTime t = parseTime(curSubstring);

                boolean hasFound = false;
                if (d != null) {
                    dtPair.add(d);
                    hasFound = true;
                }
                if (t != null) {
                    dtPair.add(t);
                    hasFound = true;
                }

                if (hasFound) {
                    if (modifier != null) {
                        dtPair.add(modifier);
                    }

                    i = j - 1;
                    break;
                }
            }
        }

        return dtPair;
    }

    private static String removeFirstWord(String curSubstring) {
        assert curSubstring != null;

        String[] split = curSubstring.split(" ", 2);
        if (split.length > 1) {
            return split[1];
        } else {
            return "";
        }
    }

    private static DateModifier getDateModifier(String curSubstring) {
        buildModifierMap();

        String possibleModifier = curSubstring.split(" ")[0].toLowerCase();
        return modifierMap.get(possibleModifier);
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

    private static ParsedDate parseDate(String dateString) {
        return DateParser.parseDate(dateString);
    }

    private static LocalTime parseTime(String timeString) {
        return TimeParser.parseTime(timeString);
    }

    public static boolean isDate(String dateString) {
        if (getDateModifier(dateString) != null) {
            dateString = removeFirstWord(dateString);
        }
        return DateParser.isDate(dateString);
    }

    public static boolean isTime(String timeString) {
        if (getDateModifier(timeString) != null) {
            timeString = removeFirstWord(timeString);
        }
        return TimeParser.isTime(timeString);
    }

    private static String removePrepositions(String string) {
        buildPrepositionSet();

        int lastFoundPreposition = 0;
        String[] tokens = string.toLowerCase().split(SYMBOL_DELIM);

        // find last preposition (e.g. "at around 3pm")
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

    private static void buildPrepositionSet() {
        if (prepositions != null) {
            return;
        }

        String[] preArr = {"about", "after", "around", "at", "before",
                "between", "by", "for", "from", "in", "near", "on", "past",
                "round", "since", "till", "to", "until", "within"};
        prepositions = new HashSet<String>(Arrays.asList(preArr));
    }

}
