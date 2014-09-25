package io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import data.TaskData;
import data.taskinfo.TaskInfo;

public class FileInputOutput {

    private static final String JSON_NAME = "name";
    private static final String JSON_DURATION = "duration";
    private static final String JSON_END_TIME = "endTime";
    private static final String JSON_END_DATE = "endDate";
    private static final String JSON_DETAILS = "details";
    private static final String JSON_TAGS = "tags";
    private static final String JSON_PRIORITY = "priority";
    private static final String JSON_STATUS = "status";
    private static final String JSON_NUMBER_OF_TIMES = "numberOfTimes";
    private static final String JSON_REPEAT_INTERVAL = "repeatInterval";
    
    private String fileName;
    private String fileHash;
    
    private final TaskData taskData;
    
    public FileInputOutput(TaskData taskData) {
        this.taskData = taskData;
    }
    
    /**
     * @return true iff there is a change in the file.
     */
    public boolean read() {
        if (fileHashMatchesPrevious())
            return false;
        
        TaskInfo[] tasks = readTasksFromFile();
        taskData.updateTaskList(tasks);
        
        return true;
    }

    /**
     * @return true for success.
     */
    public boolean write() {
        writeTasksToFile();
        
        fileHash = computeHash(fileName);
        return true;
    }
    
    
    public static String computeHash(String nameOfFile) {
        try {
            MessageDigest md5er = generateMessageDigest(nameOfFile);

            if (md5er == null) {
                return null;
            }
            
            byte[] digest = md5er.digest();
            
            if (digest == null) {
                return null;
            }
            
            StringBuilder result = new StringBuilder();
            
            for (int i = 0; i < digest.length; i++) {
                result.append(byteToHex(digest[i]));
            }
            
            return result.toString();
            
        } catch (Exception e) {
            return null;
        }
    }

    private static String byteToHex(byte b) {
        return Integer.toString((b & 0xff) + 0x100, 16).substring(1);
    }

    private static MessageDigest generateMessageDigest(String nameOfFile) {
        MessageDigest md5er;
        
        try {
            InputStream fin = new FileInputStream(nameOfFile);
            md5er = MessageDigest.getInstance("MD5");
            
            byte[] buffer = new byte[1024];
            
            int read = 0;
            while (read != -1) {
                read = fin.read(buffer);
                if (read > 0) {
                    md5er.update(buffer, 0, read);
                }
            }
            
            fin.close();
            
        } catch (IOException e) {
            return null;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        return md5er;
    }


    public static String localTimeToString(LocalTime time) {
        return String.format("%02d:%02d:%02d", time.getHour(),
                time.getMinute(), time.getSecond());
    }
    
    public static LocalTime stringToLocalTime(String timeString) {
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
        return String.format("%d-%02d-%02d", date.getYear(),
                date.getMonthValue(), date.getDayOfMonth());
    }
    
    public static LocalDate stringToLocalDate(String dateString) {
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
        return duration.toString();
    }
    
    public static Duration stringToDuration(String durationString) {
        Duration duration;
        try {
            duration = Duration.parse(durationString);
            
        } catch (DateTimeParseException e) {
            return null;
        }
        return duration;
    }
    
    
    public static String taskToJson(TaskInfo taskInfo) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(JSON_NAME, taskInfo.name);
        builder.add(JSON_DURATION, durationToString(taskInfo.duration));
        builder.add(JSON_END_TIME, localTimeToString(taskInfo.endTime));
        builder.add(JSON_END_DATE, localDateToString(taskInfo.endDate));
        builder.add(JSON_DETAILS, taskInfo.name);
        builder.add(JSON_TAGS, taskInfo.name);
        builder.add(JSON_PRIORITY, taskInfo.name);
        builder.add(JSON_STATUS, taskInfo.name);
        //builder.add(JSON_NUMBER_OF_TIMES, taskInfo.name);
        //builder.add(JSON_REPEAT_INTERVAL, taskInfo.name);
        return "JSON!!";
    }

    private TaskInfo[] readTasksFromFile() {
        
        // TODO Auto-generated method stub
        return null;
    }
    
    private void writeTasksToFile() {
        //JsonObject jsonObject;
    }

    private boolean fileHashMatchesPrevious() {
        String currentHash = computeHash(fileName);
        return fileHash.equals(currentHash);
    }
}
