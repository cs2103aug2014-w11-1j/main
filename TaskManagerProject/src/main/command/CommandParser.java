package main.command;

import java.util.ArrayList;
import java.util.List;

import data.taskinfo.Priority;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class CommandParser {
    private final static String SYMBOL_IGNORE = "\"";
    private final static String SYMBOL_TAG = "#";
    private final static String SYMBOL_PRIORITY = "+";
    private final static Priority DEFAULT_PRIORITY = Priority.LOW;

    public static TaskInfo parseTask(String taskText) {
        TaskInfo task = new TaskInfo();

        parseName(taskText, task);
        parseDateTime(taskText, task);
        parseTags(taskText, task);
        parsePriority(taskText, task);

        return task;
    }

    public static void parseName(String args, TaskInfo task) {
        task.name = args;
        // TODO Proper parsing.
    }

    public static void parseDateTime(String args, TaskInfo task) {
        args = stripIgnoredSegments(args);
        DateParser.parseDateTime(args, task);
    }

    public static void parseTags(String args, TaskInfo task) {
        // TODO Care about efficiency.
        args = stripIgnoredSegments(args);
        String[] words = args.split(" ");

        List<Tag> tags = new ArrayList<Tag>();
        for (String word : words) {
            if (word.startsWith(SYMBOL_TAG)) {
                tags.add(new Tag(removeFirstChar(word)));
            }
        }
        task.tags = tags.toArray(new Tag[tags.size()]);
    }

    public static void parsePriority(String args, TaskInfo task) {
        // TODO Care about efficiency.
        args = stripIgnoredSegments(args);
        String[] words = args.split(" ");

        Priority p = null;

        for (String word : words) {
            if (word.startsWith(SYMBOL_PRIORITY)) {
                String priorityLevel = removeFirstChar(word).toLowerCase();

                if (priorityLevel.equals("high")) {
                    p = Priority.HIGH;
                }
                if (priorityLevel.equals("med")) {
                    p = Priority.MEDIUM;
                }
                if (priorityLevel.equals("low")){
                    p = Priority.LOW;
                }

                task.priority = p;
            }
            if (p != null) {
                break;
            }
        }

        task.priority = p != null ? p : DEFAULT_PRIORITY;
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
