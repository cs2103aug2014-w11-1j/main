package main.formatting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import data.TaskId;
import data.taskinfo.Priority;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class DetailsUtility {
    private final static String FORMAT_ID = "Task [%1$s]";
    private final static String FORMAT_NAME = "Name: %1$s";
    private final static String FORMAT_TIME = "Time: %1$s";
    private final static String DATETIME_FORMAT_TIME = "HH:mm (a)";
    private final static String FORMAT_DATE = "Date: %1$s";
    private final static String DATETIME_FORMAT_DATE = "EEEE, d MMM Y";
    private final static String FORMAT_TAGS = "Tags: %1$s";
    private final static String FORMAT_PRIORITY = "Priority: %1$s";
    private final static String FORMAT_DESCRIPTION = "Description: %1$s";
    
    private String formatTime(LocalTime time) {
        DateTimeFormatter formatter = 
                DateTimeFormatter.ofPattern(DATETIME_FORMAT_TIME);
        String formattedTime = formatter.format(time);
        return String.format(FORMAT_TIME, formattedTime);
    }
    
    private String formatDate(LocalDate date) {
        DateTimeFormatter formatter = 
                DateTimeFormatter.ofPattern(DATETIME_FORMAT_DATE);
        String formattedDate = formatter.format(date);
        return String.format(FORMAT_DATE, formattedDate);
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
            case NONE :
                priorityString = "None";
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
    
    public ArrayList<String> formatToArray(TaskInfo task, TaskId taskId) {
        ArrayList<String> result = new ArrayList<String>();
        
        result.add(String.format(FORMAT_ID, taskId.toString()));
        
        String nameLine = String.format(FORMAT_NAME,  task.name);
        result.add(addIndentation(nameLine, 3));
        
        if (task.endTime != null) {
            String timeLine = formatTime(task.endTime);
            result.add(addIndentation(timeLine, 3));
        }
        
        if (task.endDate != null) { 
            String dateLine = formatDate(task.endDate);
            result.add(addIndentation(dateLine, 3));
        }
        
        if (task.tags != null) {
            String tagsLine = String.format(FORMAT_TAGS, 
                    buildTagsString(task.tags));
            result.add(addIndentation(tagsLine, 3));
        }
        
        String priorityLine = String.format(FORMAT_PRIORITY, 
                getPriorityString(task.priority));
        result.add(addIndentation(priorityLine, 3));
        
        if (task.details != null) {
            String descriptionLine = String.format(FORMAT_DESCRIPTION, 
                    task.details);
            result.add(addIndentation(descriptionLine, 3));
        }
        
        return result;
    }
}
