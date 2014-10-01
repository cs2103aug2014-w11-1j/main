package main.formatting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import main.message.EditSuccessfulMessage;
import main.message.EditSuccessfulMessage.Field;
import data.taskinfo.Priority;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

/**
 * Formatter for the EditSuccessfulMessage.
 * Format example:
 * Task name changed.
 * 
 * Task [a9f]
 *    Name: eat apples for dinner
 *    Time: 17:30 (PM)
 *    Date: Tuesday, 17 Feb 2015
 *    Tags: taskline, food
 *    Priority: High
 *    Description: I will die if I don't eat my apples!
 *    
 * @author Nathan
 *
 */
public class EditSuccessfulFormatter {
    private final static String FORMAT_ID = "Task [%1$s]";
    private final static String FORMAT_NAME = "   Name: %1$s";
    private final static String FORMAT_TIME = "HH:mm (a)";
    private final static String FORMAT_DATE = "EEEE, d MMM Y";
    private final static String FORMAT_TAGS = "Tags: %1$s";
    private final static String FORMAT_PRIORITY = "Priority: %1$s";
    private final static String FORMAT_DESCRIPTION = "Description: %1$s";
    
    private final static String CHANGED_NAME = "Task name changed.";
    private final static String CHANGED_TIME = "Time changed.";
    private final static String CHANGED_PRIORITY = "Priority changed.";
    private final static String ADDED_TAG = "Tag added.";
    private final static String DELETED_TAG = "Tag deleted.";
    private final static String CHANGED_DETAILS = "Details changed.";
    private final static String CHANGED_STATUS = "Status changd.";
    
    private String formatTime(LocalTime time) {
        DateTimeFormatter formatter = 
                DateTimeFormatter.ofPattern(FORMAT_TIME);
        return formatter.format(time);
    }
    
    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = 
                DateTimeFormatter.ofPattern(FORMAT_DATE);
        return formatter.format(date);
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
    
    private String addIndentation(String s, int numberOfSpaces) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numberOfSpaces; i++) {
            builder.append(" ");
        }
        builder.append(s);
        return builder.toString();
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
            case DETAILS :
                changedFieldString = CHANGED_DETAILS;
            case STATUS : 
                changedFieldString = CHANGED_STATUS;
        }
        return changedFieldString;
    }
    
    
    public ArrayList<String> formatToArray(EditSuccessfulMessage message) {
        ArrayList<String> result = new ArrayList<String>();
        
        for (Field field : message.getChangedField()) {
            result.add(getChangedFieldString(field));
        }
        result.add("");
        
        TaskInfo task = message.getTask();
        
        result.add(String.format(FORMAT_ID,message.getTaskId().toString()));
        
        String nameLine = String.format(FORMAT_NAME,  task.name);
        result.add(addIndentation(nameLine, 3));
        
        String timeLine = formatTime(task.endTime);
        result.add(addIndentation(timeLine, 3));
        
        String dateLine = formatDate(task.endDate);
        result.add(addIndentation(dateLine, 3));
        
        String tagsLine = String.format(FORMAT_TAGS, 
                buildTagsString(task.tags));
        result.add(addIndentation(tagsLine, 3));
        
        
        String priorityLine = String.format(FORMAT_PRIORITY, 
                getPriorityString(task.priority));
        result.add(addIndentation(priorityLine, 3));
        
        String descriptionLine = String.format(FORMAT_DESCRIPTION, 
                task.details);
        result.add(addIndentation(descriptionLine, 3));
        
        return result;
    }
    
    public String format(EditSuccessfulMessage message) {
        return null;
    }
}
