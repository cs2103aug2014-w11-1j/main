package main.command.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class CommandParser {
    private final static String SYMBOL_DELIM = " ";
    private final static String SYMBOL_IGNORE = "\"";
    private final static String SYMBOL_TAG = "#";
    private final static String SYMBOL_PRIORITY = "+";
    private final static Priority DEFAULT_PRIORITY = null;

    public static String parseName(String args) {
        String name = parseNameRecurse(args);
        String cleanedName = cleanCmdString(name).trim();
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

                    if (isPriority(curSubstring) || isTag(curSubstring) ||
                            DateParser.isDate(curSubstring) ||
                            DateParser.isTime(curSubstring)) {
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
     * Extracts the ignored segment (surrounded by SYMBOL_IGNORE) from a string.
     *
     * @param args
     *            is the string to extract from.
     * @return null if no ignored segment exists, otherwise, the ignored
     *         segment.
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

    public static void parseDateTime(String args, TaskInfo task) {
        args = stripIgnoredSegments(args);
        DateParser.parseDateTime(args, task);
    }

    public static DateTimePair parseDateTimes(String args) {
        args = stripIgnoredSegments(args);
        return DateParser.parseDateTimes(args);
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

    public static Priority[] parsePriorities(String args) {
        args = stripIgnoredSegments(args);
        args = cleanCmdString(args);
        String[] words = args.split(SYMBOL_DELIM);

        List<Priority> pList = new ArrayList<Priority>();
        for (String word : words) {
            Priority p = matchPriority(word);

            if (p != DEFAULT_PRIORITY) {
                pList.add(p);
            }
        }

        return pList.isEmpty() ? null :
            pList.toArray(new Priority[pList.size()]);
    }

    public static Priority parsePriority(String args) {
        Priority[] priorities = parsePriorities(args);
        return priorities == null ? null : priorities[0];
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
                case "none" :
                    p = Priority.NONE;
                    break;
            }
        }

        return p;
    }

    public static Status parseStatus(String args) {
        Status s = null;
        switch (args) {
            case "undone" :
                s = Status.UNDONE;
                break;
            case "done" :
                s = Status.DONE;
                break;
            default:
                break;
        }

        return s;
    }

    private static String removeFirstChar(String s) {
        return s.substring(1);
    }

    private static String cleanCmdString(String cmdString) {
        return stripExtraDelims(cmdString).trim();
    }

    private static String stripExtraDelims(String s) {
        String doubleDelim = SYMBOL_DELIM + SYMBOL_DELIM;
        while (s.contains(doubleDelim)) {
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
