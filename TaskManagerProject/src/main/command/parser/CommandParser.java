package main.command.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;

//@author A0111862M
/**
 * Utility class for Command classes to parse parts of tasks from strings.
 */
public class CommandParser {
    private final static String SYMBOL_DELIM = " ";
    private final static String SYMBOL_IGNORE = "\"";
    private final static String SYMBOL_TAG = "#";
    private final static String SYMBOL_PRIORITY = "+";

    /**
     * Parses a string into one suitable for a task name, removing all tags,
     * dates, times, priorities, and statuses.
     *
     * @param args
     *            the string to be parsed into a task name
     * @return the parsed task name
     */
    public static String parseName(String args) {
        String name = parseNameRecurse(args);
        String cleanedName = cleanCmdString(name).trim();
        return cleanedName;
    }

    /**
     * Parses a string into one suitable for a task name recursively, splitting
     * it into segments based on parts that should be parsed directly as the
     * task name (ignored segments).
     *
     * @param args
     *            the string to be parsed into a task name
     * @return the parsed part of the task name
     */
    private static String parseNameRecurse(String args) {
        if (hasIgnoredSegment(args)) {
            String ignoredSegment = getFirstIgnoredSegment(args);
            int startIgnoreIdx = args.indexOf(ignoredSegment);
            int endIgnoreIdx = startIgnoreIdx + ignoredSegment.length();

            // recursively parse non-ignored segments
            String front = args.substring(0, startIgnoreIdx).trim();
            front = parseNameRecurse(front);
            String back = args.substring(endIgnoreIdx).trim();
            back = parseNameRecurse(back);

            // remove SYMBOL_IGNORE from both sides
            String cleanedIgnoredSegment =
                    ignoredSegment.substring(1, ignoredSegment.length() - 1);
            return front + " " + cleanedIgnoredSegment + " " + back;
        } else {
            // check if any sequence of tokens should not be in the task name
            String[] tokens = args.split(SYMBOL_DELIM);
            BitSet toRemove = new BitSet(); // indices to be removed

            for (int i = 0; i < tokens.length; i++) {
                for (int j = tokens.length; j > i; j--) {
                    String[] curTokens = Arrays.copyOfRange(tokens, i, j);
                    String curSubstring = String.join(SYMBOL_DELIM, curTokens);

                    if (!isKeyword(curSubstring)) {
                        toRemove.set(i, j);
                        break;
                    }
                }
            }

            // join the tokens that should be kept
            StringBuilder cleanedName = new StringBuilder();
            for (int i = toRemove.nextClearBit(0); i < tokens.length;
                    i = toRemove.nextClearBit(i + 1)) {
                cleanedName.append(tokens[i]);
                cleanedName.append(SYMBOL_DELIM);
            }
            return cleanedName.toString().trim();
        }
    }

    /**
     * Checks if a string is a keyword, i.e., cannot be parsed into a priority,
     * tag, status, date, or time.
     *
     * @param substring
     *            the string to be checked
     * @return false if the string can be parsed into one of those parts,
     *         true otherwise
     */
    private static boolean isKeyword(String substring) {
        boolean isNotKeyword = isPriority(substring) || isTag(substring) ||
                isStatus(substring) || DateTimeParser.isDate(substring) ||
                DateTimeParser.isTime(substring);
        return !isNotKeyword;
    }

    /**
     * Extracts the first ignored segment (surrounded by SYMBOL_IGNORE) from
     * a string.
     *
     * @param args
     *            the string to extract from
     * @return null if no ignored segment exists, otherwise, the first ignored
     *         segment (substring)
     */
    private static String getFirstIgnoredSegment(String args) {
        int startIdx = args.indexOf(SYMBOL_IGNORE);
        int endIdx = args.indexOf(SYMBOL_IGNORE, startIdx + 1);

        if (endIdx != -1) {
            String ignoredSegment = args.substring(startIdx, endIdx + 1);
            return ignoredSegment;
        }

        return null;
    }

    private static boolean hasIgnoredSegment(String args) {
        return getFirstIgnoredSegment(args) != null;
    }

    /**
     * Parses dates and times from {@code args} into a DateTimePair.
     * This method parses the dates and times in sequence, so
     * "5 Nov to 6 Nov 3pm" will be parsed with 3pm in the second time instead
     * of the first.
     *
     * @param args
     *            the string possibly containing dates and times
     * @return a DateTimePair representing the dates and times found
     */
    public static DateTimePair parseDateTimesInSequence(String args) {
        args = stripIgnoredSegments(args);
        return DateTimeParser.parseDateTimesInSequence(args);
    }

    /**
     * Parses dates and times from {@code args} into a DateTimePair.
     * This method does not parses the dates and times in sequence, so
     * "5 Nov to 6 Nov 3pm" will be parsed with 3pm in the first time instead of
     * the second.
     *
     * @param args
     *            the string possibly containing dates and times
     * @return a DateTimePair representing the dates and times found
     */
    public static DateTimePair parseDateTimes(String args) {
        args = stripIgnoredSegments(args);
        return DateTimeParser.parseDateTimes(args);
    }

    /**
     * Parses tags from {@code args} into an array of {@code Tag}s.
     *
     * @param args
     *            the string possibly containing tags
     * @return an array of Tags found, null if no Tag was found
     */
    public static Tag[] parseTags(String args) {
        args = stripIgnoredSegments(args);
        args = cleanCmdString(args);
        String[] words = args.split(SYMBOL_DELIM);

        List<Tag> tagList = new ArrayList<Tag>();
        for (String word : words) {
            if (isTag(word)) {
                // remove the SYMBOL_TAG first
                word = removeFirstChar(word);
                tagList.add(new Tag(word));
            }
        }
        Tag[] tags = tagList.toArray(new Tag[tagList.size()]);

        return tags.length == 0 ? null : tags;
    }

    /**
     * Checks if {@code possibleTag} is a tag.
     *
     * @param possibleTag
     *            the string is that possibly a tag
     * @return true if possibleTag is a tag, false otherwise
     */
    private static boolean isTag(String possibleTag) {
        return possibleTag.startsWith(SYMBOL_TAG) &&
                possibleTag.length() > 1 &&
                !possibleTag.contains(SYMBOL_DELIM);
    }

    /**
     * Parses priorities from {@code args} into an array of {@code Priority}s.
     *
     * @param args
     *            the string possibly containing priorities
     * @return an array of priorities found, null if no Priority was found
     */
    public static Priority[] parsePriorities(String args) {
        args = stripIgnoredSegments(args);
        args = cleanCmdString(args);
        String[] words = args.split(SYMBOL_DELIM);

        List<Priority> pList = new ArrayList<Priority>();
        for (String word : words) {
            if (isPriority(word)) {
                pList.add(matchPriority(word));
            }
        }
        Priority[] priorities = pList.toArray(new Priority[pList.size()]);

        return priorities.length == 0 ? null : priorities;
    }

    /**
     * Parses the first priority found in {@code args}.
     *
     * @param args
     *            the string possibly containing a priority
     * @return the first priority found, null if no priorities found
     */
    public static Priority parsePriority(String args) {
        Priority[] priorities = parsePriorities(args);
        return priorities == null ? null : priorities[0];
    }

    /**
     * Checks if {@code possiblePriority} is a priority.
     *
     * @param possiblePriority
     *            the string that is possibly a priority
     * @return true is possiblePriority is a priority, false otherwise
     */
    private static boolean isPriority(String possiblePriority) {
        boolean isPriority = matchPriority(possiblePriority) != null;
        return isPriority;
    }

    /**
     * Tries to convert {@code possiblePriority} into a priority.
     *
     * @param possiblePriority
     *            the string that is possibly a priority
     * @return the priority converted or null if invalid
     */
    private static Priority matchPriority(String possiblePriority) {
        Priority p = null;

        if (possiblePriority.startsWith(SYMBOL_PRIORITY) &&
                !possiblePriority.contains(SYMBOL_DELIM)) {
            String type = removeFirstChar(possiblePriority);
            try {
                p = Priority.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                // do nothing
            }
        }

        return p;
    }

    /**
     * Parses statuses from {@code args} into an array of {@code Status}es.
     *
     * @param args
     *            the string possibly containing statuses
     * @return an array of Statuses found, null if no status was found
     */
    public static Status[] parseStatuses(String args) {
        args = stripIgnoredSegments(args);
        args = cleanCmdString(args);
        String[] words = args.split(SYMBOL_DELIM);

        List<Status> sList = new ArrayList<Status>();
        for (String word : words) {
            if (isStatus(word)) {
                sList.add(parseStatus(word));
            }
        }

        return sList.isEmpty() ? null :
            sList.toArray(new Status[sList.size()]);
    }

    /**
     * Tries to convert {@code possibleStatus} into a status.
     *
     * @param possibleStatus
     *            the string that is possibly a status
     * @return the status converted or null if invalid
     */
    public static Status parseStatus(String possibleStatus) {
        try {
            return Status.valueOf(possibleStatus.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Checks if {@code possibleStatus} is a status.
     *
     * @param possibleStatus
     *            the string is that possibly a status
     * @return true if possibleStatus is a status, false otherwise
     */
    private static boolean isStatus(String possibleStatus) {
        return parseStatus(possibleStatus) != null;
    }

    /**
     * Removes the first character from a string and returns it
     */
    private static String removeFirstChar(String s) {
        return s.substring(1);
    }

    /**
     * Cleans a command string to make it suitable for parsing.
     */
    private static String cleanCmdString(String cmdString) {
        return stripExtraDelims(cmdString).trim();
    }

    /**
     * Strips occurrences of double delimiters from a string and returns it.
     */
    private static String stripExtraDelims(String s) {
        String doubleDelim = SYMBOL_DELIM + SYMBOL_DELIM;
        while (s.contains(doubleDelim)) {
            s = s.replace(doubleDelim, SYMBOL_DELIM);
        }
        return s;
    }

    /**
     * Strips pairs of ignore symbols from a string.
     *
     * @param args
     *            the string possibly containing ignore symbols
     * @return the string with pairs of ignore symbols stripped
     */
    public static String stripIgnoreSymbols(String args) {
        if (hasIgnoredSegment(args)) {
            String ignoredSegment = getFirstIgnoredSegment(args);
            int startIgnoreIdx = args.indexOf(ignoredSegment);
            int endIgnoreIdx = startIgnoreIdx + ignoredSegment.length();

            // recursively parse non-ignored segments
            String front = args.substring(0, startIgnoreIdx).trim();
            String back = args.substring(endIgnoreIdx).trim();
            back = stripIgnoreSymbols(back);

            // remove SYMBOL_IGNORE from both sides
            String cleanedIgnoredSegment =
                    ignoredSegment.substring(1, ignoredSegment.length() - 1);
            return (front + " " + cleanedIgnoredSegment + " " + back).trim();
        }
        return args.trim();
    }

    /**
     * Strips ignored segments from a string.
     *
     * @param s
     *            the string to strip ignored segments from
     * @return the string with ignored segments stripped
     */
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
