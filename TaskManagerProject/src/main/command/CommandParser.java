package main.command;

import java.util.ArrayList;
import java.util.List;

import data.taskinfo.Priority;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class CommandParser {
    private final static String SYMBOL_DELIM = " ";
    private final static String SYMBOL_IGNORE = "\"";
    private final static String SYMBOL_TAG = "#";
    private final static String SYMBOL_PRIORITY = "+";
    private final static Priority DEFAULT_PRIORITY = Priority.NONE;

    public static TaskInfo parseTask(String taskText) {
        TaskInfo task = TaskInfo.create();

        task.name = parseName(taskText);
        parseDateTime(taskText, task);
        task.tags = parseTags(taskText);
        task.priority = parsePriority(taskText);

        return task;
    }

    public static String parseName(String args) {
        return args;
        // TODO Proper parsing.
    }

    public static void parseDateTime(String args, TaskInfo task) {
        args = stripIgnoredSegments(args);
        DateParser.parseDateTime(args, task);
    }

    public static Tag[] parseTags(String args) {
        args = stripIgnoredSegments(args);
        String[] words = args.split(SYMBOL_DELIM);

        List<Tag> tagList = new ArrayList<Tag>();
        for (String word : words) {
            if (word.startsWith(SYMBOL_TAG)) {
                word = removeFirstChar(word);
                if (!word.isEmpty()) {
                    tagList.add(new Tag(word));
                }
            }
        }
        Tag[] tags = tagList.toArray(new Tag[tagList.size()]);

        return tags.length == 0 ? null : tags;
    }

    public static Priority parsePriority(String args) {
        args = stripIgnoredSegments(args);
        String[] words = args.split(SYMBOL_DELIM);

        Priority p = DEFAULT_PRIORITY;

        for (String word : words) {
            if (word.startsWith(SYMBOL_PRIORITY)) {
                String priorityLevel = removeFirstChar(word).toLowerCase();

                if (priorityLevel.equals("high")) {
                    p = Priority.HIGH;
                }
                if (priorityLevel.equals("med")) {
                    p = Priority.MEDIUM;
                }
                if (priorityLevel.equals("low")) {
                    p = Priority.LOW;
                }
            }
            if (p != Priority.NONE) {
                break;
            }
        }

        return p;
    }

    private static String removeFirstChar(String s) {
        return s.substring(1);
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
