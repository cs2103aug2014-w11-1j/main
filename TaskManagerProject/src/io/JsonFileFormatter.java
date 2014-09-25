package io;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.json.stream.JsonParsingException;

import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class JsonFileFormatter {

    private static final String FORMAT_TIME = "%02d:%02d:%02d";
    private static final String FORMAT_DATE = "%d-%02d-%02d";
    private static final String ERROR_WRONG_EVENT = "Wrong event: ";
    private static final String ERROR_UNKNOWN_ELEMENT = "Unknown element: ";
    private static final String STRING_EMPTY = "";
    private static final String STRING_NULL = "null";
    
    private static final String JSON_TASKS = "tasks";
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
    
    
    /**
     * @param taskInfos an Array of TaskInfo to be converted into a string.
     * @return a String containing all the data from the taskInfos array,
     * in JSON format, pretty printed.
     */
    public static String tasksToJsonString(TaskInfo[] taskInfos) {
        StringWriter stringWriter = new StringWriter();
        tasksToJson(stringWriter, taskInfos);
        
        return stringWriter.toString();
    }

    /**
     * @param jsonString A string formatted in Json storing the TaskInfos
     * @return an array of TaskInfos extracted from jsonString
     * @throws InvalidFileFormatException
     * thrown when an error occurred during the parsing of the string.
     */
    public static TaskInfo[] jsonStringToTasks(String jsonString)
            throws InvalidFileFormatException {
        
        StringReader stringReader = new StringReader(jsonString);
        return jsonToTasks(stringReader);
    }

    /**
     * 
     * @param writer Outputs the JSON string to this writer.
     * @param taskInfos an Array of TaskInfo to be converted into JSON format
     * @return true iff successful.
     */
    public static boolean writeTasksToJson(Writer writer, TaskInfo[] taskInfos) {
        tasksToJson(writer, taskInfos);
        return true;
    }

    /**
     * @param reader A Reader to read a JSON string from.
     * @return an array of TaskInfos extracted from parsing the JSON data in
     * the reader.
     */
    public static TaskInfo[] readTasksFromJson(Reader reader) {
        TaskInfo[] taskInfos = null;
        try {
            taskInfos = jsonToTasks(reader);
            
        } catch (InvalidFileFormatException e) {
            e.printStackTrace();
            return null;
        }
        return taskInfos;
    }
    
    
    
    
    
    
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
            return STRING_NULL;
        
        return status.name();
    }

    public static String priorityToString(Priority priority) {
        if (priority == null)
            return STRING_NULL;
        
        return priority.name();
    }

    public static Status stringToStatus(String statusString) {
        if (isNullString(statusString))
            return null;
        
        return Status.valueOf(statusString);
    }

    public static Priority stringToPriority(String priorityString) {
        if (isNullString(priorityString))
            return null;
        
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
    
    
    
    private static TaskInfo parseTaskInfo(JsonParser parser)
            throws InvalidFileFormatException{
        
        TaskInfo taskInfo = new TaskInfo();
        
        Event event = parser.next();
        while (event != Event.END_OBJECT) {
            
            if (event != Event.KEY_NAME) {
                throw new InvalidFileFormatException(ERROR_WRONG_EVENT + event.name());
            }
            String key = parser.getString();
            
            event = parser.next();
            if (event == Event.VALUE_STRING) {
                String value = parser.getString();
                readKeyValuePair(taskInfo, key, value);
                
            } else if (event == Event.START_ARRAY) {
                readKeyStringArrayPair(taskInfo, key, parser);
            
            } else {
                throw new InvalidFileFormatException(ERROR_WRONG_EVENT + event.name());
            }
            
            event = parser.next();
        }
        
        return taskInfo;
    }
    
    private static void readKeyValuePair(TaskInfo taskInfo,
            String key, String value) throws InvalidFileFormatException {
        
        switch(key) {
            case JSON_NAME :
                taskInfo.name = jsonStringToString(value);
                break;
            case JSON_DURATION :
                taskInfo.duration = stringToDuration(value);
                break;
            case JSON_END_TIME :
                taskInfo.endTime = stringToLocalTime(value);
                break;
            case JSON_END_DATE :
                taskInfo.endDate = stringToLocalDate(value);
                break;
            case JSON_TAGS :
                if (isNullString(value))
                    taskInfo.tags = null;
                else
                    throw new InvalidFileFormatException("Unable to read tags");
                break;
            case JSON_DETAILS :
                taskInfo.details = jsonStringToString(value);
                break;
            case JSON_PRIORITY :
                taskInfo.priority = stringToPriority(value);
                break;
            case JSON_STATUS :
                taskInfo.status = stringToStatus(value);
                break;
            default :
                throw new InvalidFileFormatException(ERROR_UNKNOWN_ELEMENT + key);
        }
    }
    
    private static void readKeyStringArrayPair(TaskInfo taskInfo, String key,
            JsonParser parser) throws InvalidFileFormatException {
        
        Queue<String> stringQueue = new LinkedList<>();
        
        Event event = parser.next();
        while (event != Event.END_ARRAY) {
            stringQueue.offer(parser.getString());
            event = parser.next();
        }
        
        switch(key) {
            case JSON_TAGS:
                Tag[] tags = new Tag[stringQueue.size()];
                for (int i = 0; i < tags.length; i++) {
                    tags[i] = new Tag(stringQueue.poll());
                }
                taskInfo.tags = tags;
                break;
            default:
                throw new InvalidFileFormatException(ERROR_UNKNOWN_ELEMENT + key);
        }
    }

    private static JsonObjectBuilder createJsonObjectBuilder(TaskInfo taskInfo) {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        builder.add(JSON_NAME, stringToJsonString(taskInfo.name));
        builder.add(JSON_DURATION, durationToString(taskInfo.duration));
        builder.add(JSON_END_TIME, localTimeToString(taskInfo.endTime));
        builder.add(JSON_END_DATE, localDateToString(taskInfo.endDate));
        builder.add(JSON_DETAILS, stringToJsonString(taskInfo.details));
        builderAddTags(taskInfo.tags, builder);
        builder.add(JSON_PRIORITY, priorityToString(taskInfo.priority));
        builder.add(JSON_STATUS, statusToString(taskInfo.status));
        
        // PENDING IMPLEMENTATION
        //builder.add(JSON_NUMBER_OF_TIMES, taskInfo.numberOfTimes);
        //builder.add(JSON_REPEAT_INTERVAL, taskInfo.repeatInterval);
        
        return builder;
    }

    private static boolean isNullString(String value) {
        return STRING_NULL.equals(value);
    }

    private static void builderAddTags(Tag[] tags, JsonObjectBuilder builder) {
        if (tags == null) {
            builder.add(JSON_TAGS, STRING_NULL);
            
        } else {
            JsonArrayBuilder tagArrayJson = Json.createArrayBuilder();
            for (Tag tag : tags) {
                tagArrayJson.add(tag.toString());
            }
            
            builder.add(JSON_TAGS, tagArrayJson);
        }
    }
    
    private static void tasksToJson(Writer writer, TaskInfo[] taskInfos) { 
        
        JsonObjectBuilder builder = Json.createObjectBuilder();
        
        JsonArrayBuilder taskInfoArrayJson = Json.createArrayBuilder();
        for (TaskInfo taskInfo : taskInfos) {
            taskInfoArrayJson.add(createJsonObjectBuilder(taskInfo));
        }
        
        builder.add(JSON_TASKS, taskInfoArrayJson);
        
        JsonObject jsonObject = builder.build();
        writePrettyPrint(writer, jsonObject);
    }
    
    private static TaskInfo[] jsonToTasks(Reader reader)
            throws InvalidFileFormatException {
        
        Queue<TaskInfo> taskInfoQueue = new LinkedList<>();
        
        JsonParser parser = Json.createParser(reader);
        try {
            Event event = parser.next();
            
            while (!(event == Event.KEY_NAME && parser.getString().equals(JSON_TASKS))) {
                event = parser.next();
            }
            
            event = parser.next();
            if (event != Event.START_ARRAY) {
                throw new InvalidFileFormatException("Tasks is not an array.");
            }
            
            event = parser.next();
            while (event == Event.START_OBJECT) {
                taskInfoQueue.offer(parseTaskInfo(parser));
                event = parser.next();
            } 
            
            if (event == Event.END_ARRAY) {
                parser.close();
                
            } else {
                throw new InvalidFileFormatException("End of tasks array not found");
            }
            
        } catch (NoSuchElementException e) {
            throw new InvalidFileFormatException("Reached end of file unexpectedly.");
        } catch (JsonParsingException e) {
            throw new InvalidFileFormatException("Invalid JSON encountered.");
        } catch (JsonException e) {
            throw new InvalidFileFormatException("Unable to read file.");
        }
        
        TaskInfo[] taskInfos = new TaskInfo[taskInfoQueue.size()];
        for (int i = 0; i < taskInfos.length; i++) {
            taskInfos[i] = taskInfoQueue.poll();
        }
        
        return taskInfos;
    }
    
    private static void writePrettyPrint(Writer writer, JsonObject jsonObject) {
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        JsonWriter jsonWriter = writerFactory.createWriter(writer);
        jsonWriter.writeObject(jsonObject);
        jsonWriter.close();
    }
}
