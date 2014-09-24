package main.command;

import java.util.ArrayList;
import java.util.List;

import data.taskinfo.Priority;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class CommandParser {
    private final static String PREFIX_IGNORE = "\"";
    private final static String PREFIX_TAG = "#";
    private final static String PREFIX_PRIORITY = "+";
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
        // TODO Auto-generated method stub

    }

    public static void parseTags(String args, TaskInfo task) {
        // TODO Care about efficiency.
        args = stripIgnoredSegments(args);
        String[] words = args.split(" ");
        List<Tag> tags = new ArrayList<Tag>();
        for (String word : words) {
            if (word.startsWith(PREFIX_TAG)) {
                tags.add(new Tag(removeLeadingCharacter(word)));
            }
        }
        task.tags = (Tag[]) tags.toArray();
    }

    public static void parsePriority(String args, TaskInfo task) {
        // TODO Care about efficiency.
        args = stripIgnoredSegments(args);
        String[] words = args.split(" ");
        for (String word : words) {
            if (word.startsWith(PREFIX_PRIORITY)) {
                Priority p = DEFAULT_PRIORITY;

                String priorityLevel = removeLeadingCharacter(word).toLowerCase();
                if (priorityLevel.equals("high")) {
                    p = Priority.HIGH;
                }
                if (priorityLevel.equals("med")) {
                    p = Priority.MEDIUM;
                }

                task.priority = p;
            }
        }
    }

    private static String removeLeadingCharacter(String s) {
        return s.substring(1);
    }

    private static String stripIgnoredSegments(String s) {
        StringBuilder sB = new StringBuilder(s);
        while (sB.indexOf(PREFIX_IGNORE) > -1) {
            int startIgnoreIdx = s.indexOf(PREFIX_IGNORE);
            int endIgnoreIdx = s.indexOf(PREFIX_IGNORE, startIgnoreIdx + 1);
            sB.delete(startIgnoreIdx, endIgnoreIdx + 1);
        }
        return sB.toString();
    }

}
