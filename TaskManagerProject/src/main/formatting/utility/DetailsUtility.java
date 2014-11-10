package main.formatting.utility;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import data.TaskId;
import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

/**
 * A utility class to format the details of a TaskInfo.
 */
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
    private final static String FORMAT_STATUS = "Status: %1$s";
    
    private final static String FORMAT_START_DATETIME = "Start: %1$s";
    private final static String FORMAT_END_DATETIME = "End: %1$s";
    private final static String DATETIME_FORMAT_DATETIME = "E, d MMM Y, HH:mm (a)";

    private final static String ERROR_PRIORITY_NULL = "Priority is null.";
    private final static String ERROR_STATUS_NULL = "Status is null.";

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
    
    private String formatStartDateTime(LocalDate date, LocalTime time) {
        String formattedDateTime;
        
        if (time == null) {
            formattedDateTime = formatDate(date);
        } else {
            LocalDateTime datetime = date.atTime(time);
            
            DateTimeFormatter formatter = 
                    DateTimeFormatter.ofPattern(DATETIME_FORMAT_DATETIME);
            formattedDateTime = formatter.format(datetime);
        }
        
        return String.format(FORMAT_START_DATETIME, formattedDateTime);
    }
    
    private String formatEndDateTime(LocalDate date, LocalTime time) {
        String formattedDateTime;
        
        if (time == null) {
            formattedDateTime = formatDate(date);
        } else {
            LocalDateTime datetime = date.atTime(time);
            
            DateTimeFormatter formatter = 
                    DateTimeFormatter.ofPattern(DATETIME_FORMAT_DATETIME);
            formattedDateTime = formatter.format(datetime);
        }
        
        return String.format(FORMAT_END_DATETIME, formattedDateTime);
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
        assert priority != null : ERROR_PRIORITY_NULL;
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

    private String getStatusString(Status status) {
        assert status != null : ERROR_STATUS_NULL;

        String statusString = "";
        switch (status) {
            case DONE :
                statusString = "Done";
                break;
            case UNDONE :
                statusString = "Not done";
                break;
        }
        return statusString;
    }

    private String addIndentation(String s, int numberOfSpaces) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numberOfSpaces; i++) {
            builder.append(" ");
        }
        builder.append(s);
        return builder.toString();
    }
    
    boolean isEndOnly(TaskInfo task) {
        return task.startDate == null && task.startTime == null;
    }

    /**
     * Format a pair of TaskInfo and TaskId to an ArrayList of String, where
     * each entry of the ArrayList corresponds to a line of output.
     * @param task The TaskInfo to be formatted.
     * @param taskId The TaskId to be formatted.
     * @return The formatted task details.
     */
    public ArrayList<String> formatToArray(TaskInfo task, TaskId taskId) {
        ArrayList<String> result = new ArrayList<String>();

        result.add(String.format(FORMAT_ID, taskId.toString()));

        String nameLine = String.format(FORMAT_NAME,  task.name);
        result.add(addIndentation(nameLine, 3));

        if (isEndOnly(task)) {
            if (task.endTime != null) {
                String timeLine = formatTime(task.endTime);
                result.add(addIndentation(timeLine, 3));
            }
            if (task.endDate != null) {
                String dateLine = formatDate(task.endDate);
                result.add(addIndentation(dateLine, 3));
            }
        } else {
            String startLine = formatStartDateTime(task.startDate, task.startTime);
            String endLine = formatEndDateTime(task.endDate, task.endTime);
            
            result.add(addIndentation(startLine, 3));
            result.add(addIndentation(endLine, 3));
        }

        if (task.tags != null) {
            String tagsLine = String.format(FORMAT_TAGS,
                    buildTagsString(task.tags));
            result.add(addIndentation(tagsLine, 3));
        }

        String priorityLine = String.format(FORMAT_PRIORITY,
                getPriorityString(task.priority));
        result.add(addIndentation(priorityLine, 3));

        String statusLine = String.format(FORMAT_STATUS, getStatusString(task.status));
        result.add(addIndentation(statusLine, 3));

        if (task.details != null) {
            String descriptionLine = String.format(FORMAT_DESCRIPTION,
                    task.details);
            result.add(addIndentation(descriptionLine, 3));
        }

        return result;
    }
}
