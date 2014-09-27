package main.formatting;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import main.message.EditSuccessfulMessage;
import data.taskinfo.Priority;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class EditSuccessfulFormatter {
    private final static String FORMAT_ID = "Task []";
    private final static String FORMAT_NAME = "   Name: %1$s";
    private final static String DATETIME_FORMAT_TIME = "HH:mm (a)";
    private final static String FORMAT_TAGS = "Tags: %1$s";
    private final static String FORMAT_PRIORITY = "Priority: %1$s";
    private final static String FORMAT_DESCRIPTION = "Description: %1$s";
    
    private final static String CHANGED_NAME = "Task name changed.";
    private final static String CHANGED_TIME = "Time changed.";
    private final static String CHANGED_PRIORITY = "Priority changed.";
    private final static String ADDED_TAG = "Tag added.";
    private final static String DELETED_TAG = "Tag deleted.";
    
    private String formatTime(LocalTime time) {
        DateTimeFormatter formatter = 
                DateTimeFormatter.ofPattern(DATETIME_FORMAT_TIME);
        return formatter.format(time);
    }
    
    private String buildTagsString(Tag[] tags) {
        StringBuilder builder = new StringBuilder("");
        for (int i = 0; i < tags.length; i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(tags[i].toString());
        }
        return builder.toString();
    }
    
    private String getPriorityString(Priority priority) {
        String priorityString = "";
        switch(priority) {
            case HIGH :
                priorityString = "High";
                break;
            case MEDIUM :
                priorityString = "Medium";
                break;
            case LOW :
                priorityString = "Low";
                break;
        }
        return priorityString;
    }
    
    private String getChangedFieldString(
            EditSuccessfulMessage.Field changedField) {
        String changedFieldString = "";
        switch(changedField) {
            case NAME :
                changedFieldString = CHANGED_NAME;
                break;
            case PRIORITY :
                changedFieldString = CHANGED_PRIORITY;
                break;
            case TIME :
                changedFieldString = CHANGED_TIME;
                break;
            case TAGS_ADD :
                changedFieldString = ADDED_TAG;
                break;
            case TAGS_DELETE :
                changedFieldString = DELETED_TAG;
                break;
        }
        return changedFieldString;
    }
    
    
    public ArrayList<String> formatToArray(EditSuccessfulMessage message) {
        ArrayList<String> result = new ArrayList<String>();
        
        result.add(getChangedFieldString(message.getChangedField()));
        result.add("");
        
        TaskInfo task = message.getTask();
        
        result.add(FORMAT_ID);
        result.add(String.format(FORMAT_NAME, task.name));
        result.add(formatTime(task.endTime));
        result.add(String.format(FORMAT_TAGS, buildTagsString(task.tags)));
        result.add(String.format(FORMAT_PRIORITY, 
                getPriorityString(task.priority)));
        result.add(String.format(FORMAT_DESCRIPTION, task.details));
        
        return result;
    }
    
    public String format(EditSuccessfulMessage message) {
        return null;
    }
}
