package io.json;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import data.taskinfo.Priority;
import data.taskinfo.Status;

public class JsonItemParser {
    
    public static final String STRING_EMPTY = "";
    public static final String STRING_NULL = "null";

    private static final String FORMAT_TIME = "%02d:%02d:%02d";
    private static final String FORMAT_DATE = "%d-%02d-%02d";

    public static String localTimeToString(LocalTime time) {
        if (time == null)
            return STRING_NULL;
        
        return String.format(FORMAT_TIME, time.getHour(),
                time.getMinute(), time.getSecond());
    }
    
    public static LocalTime stringToLocalTime(String timeString) {
        if (isNullString(timeString))
            return null;
        
        String[] split = timeString.split(":");
        LocalTime time;
        try {
            int hour = Integer.parseInt(split[0]);
            int minute = Integer.parseInt(split[1]);
            int second = Integer.parseInt(split[2]);
            time = LocalTime.of(hour, minute, second);
            
        } catch (NumberFormatException e) {
            return null;
        }
        return time;
    }

    public static String localDateToString(LocalDate date) {
        if (date == null)
            return STRING_NULL;
        
        return String.format(FORMAT_DATE, date.getYear(),
                date.getMonthValue(), date.getDayOfMonth());
    }
    
    public static LocalDate stringToLocalDate(String dateString) {
        if (isNullString(dateString))
            return null;
        
        String[] split = dateString.split("\\-");
        LocalDate date;
        try {
            int year = Integer.parseInt(split[0]);
            int month = Integer.parseInt(split[1]);
            int dayOfMonth = Integer.parseInt(split[2]);
            date = LocalDate.of(year, month, dayOfMonth);
            
        } catch (NumberFormatException e) {
            return null;
        }
        return date;
        
    }

    public static String durationToString(Duration duration) {
        if (duration == null)
            return STRING_NULL;
        
        return duration.toString();
    }
    
    public static Duration stringToDuration(String durationString) {
        if (isNullString(durationString))
            return null;
        
        Duration duration;
        try {
            duration = Duration.parse(durationString);
            
        } catch (DateTimeParseException e) {
            return null;
        }
        return duration;
    }
    
    public static String statusToString(Status status) {
        if (status == null)
            return Status.defaultStatus().name();
        
        return status.name();
    }

    public static String priorityToString(Priority priority) {
        if (priority == null)
            return Priority.defaultPriority().name();
        
        return priority.name();
    }

    public static Status stringToStatus(String statusString) {
        if (isNullString(statusString))
            return Status.defaultStatus();
        
        return Status.valueOf(statusString);
    }

    public static Priority stringToPriority(String priorityString) {
        if (isNullString(priorityString))
            return Priority.defaultPriority();
        
        return Priority.valueOf(priorityString);
    }

    public static String stringToJsonString(String string) {
        if (string == null)
            return STRING_EMPTY;
        return string;
    }
    
    public static String jsonStringToString(String jsonString) {
        return jsonString;
    }

    
    public static boolean isNullString(String value) {
        return STRING_NULL.equals(value);
    }
}
