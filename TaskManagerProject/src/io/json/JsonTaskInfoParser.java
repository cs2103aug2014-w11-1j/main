package io.json;

import io.InvalidFileFormatException;

import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.stream.JsonParser;
import javax.json.stream.JsonParser.Event;
import javax.json.stream.JsonParsingException;

import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

//@author A0065475X
public class JsonTaskInfoParser {

    private static final String JSON_TASKS = "tasks";
    private static final String JSON_NAME = "name";
    private static final String JSON_START_TIME = "startTime";
    private static final String JSON_START_DATE = "startDate";
    private static final String JSON_END_TIME = "endTime";
    private static final String JSON_END_DATE = "endDate";
    private static final String JSON_DETAILS = "details";
    private static final String JSON_TAGS = "tags";
    private static final String JSON_PRIORITY = "priority";
    private static final String JSON_STATUS = "status";
    private static final String JSON_NUMBER_OF_TIMES = "numberOfTimes";
    private static final String JSON_REPEAT_INTERVAL = "repeatInterval";

    
    public static void tasksToJson(Writer writer, TaskInfo[] taskInfos) { 
        
        JsonObjectBuilder builder = Json.createObjectBuilder();
        
        JsonArrayBuilder taskInfoArrayJson = Json.createArrayBuilder();
        for (TaskInfo taskInfo : taskInfos) {
            taskInfoArrayJson.add(createJsonObjectBuilder(taskInfo));
        }
        
        builder.add(JSON_TASKS, taskInfoArrayJson);
        
        JsonObject jsonObject = builder.build();
        JsonItemParser.writePrettyPrint(writer, jsonObject);
    }
    
    public static TaskInfo[] jsonToTasks(Reader reader)
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
    
    
    private static TaskInfo parseTaskInfo(JsonParser parser)
            throws InvalidFileFormatException {
        
        TaskInfo taskInfo = TaskInfo.create();
        
        Event event = parser.next();
        while (event != Event.END_OBJECT) {
            
            if (event != Event.KEY_NAME) {
                throw new InvalidFileFormatException(
                        JsonReaderWriter.ERROR_WRONG_EVENT + event.name());
            }
            String key = parser.getString();
            
            event = parser.next();
            if (event == Event.VALUE_STRING) {
                String value = parser.getString();
                readKeyValuePair(taskInfo, key, value);
                
            } else if (event == Event.START_ARRAY) {
                readKeyStringArrayPair(taskInfo, key, parser);
            
            } else {
                throw new InvalidFileFormatException(
                        JsonReaderWriter.ERROR_WRONG_EVENT + event.name());
            }
            
            event = parser.next();
        }
        
        return taskInfo;
    }
    
    private static void readKeyValuePair(TaskInfo taskInfo,
            String key, String value) throws InvalidFileFormatException {
        
        switch(key) {
            case JSON_NAME :
                taskInfo.name = JsonItemParser.jsonStringToString(value);
                break;
            case JSON_START_TIME :
                taskInfo.startTime = JsonItemParser.stringToLocalTime(value);
                break;
            case JSON_START_DATE :
                taskInfo.startDate = JsonItemParser.stringToLocalDate(value);
                break;
            case JSON_END_TIME :
                taskInfo.endTime = JsonItemParser.stringToLocalTime(value);
                break;
            case JSON_END_DATE :
                taskInfo.endDate = JsonItemParser.stringToLocalDate(value);
                break;
            case JSON_TAGS :
                if (JsonItemParser.isNullString(value))
                    taskInfo.tags = null;
                else
                    throw new InvalidFileFormatException("Unable to read tags");
                break;
            case JSON_DETAILS :
                taskInfo.details = JsonItemParser.jsonStringToString(value);
                break;
            case JSON_PRIORITY :
                try {
                    taskInfo.priority = JsonItemParser.stringToPriority(value);
                } catch (IllegalArgumentException e) {
                    throw new InvalidFileFormatException("Unable to read priority");
                }
                break;
            case JSON_STATUS :
                try {
                    taskInfo.status = JsonItemParser.stringToStatus(value);
                } catch (IllegalArgumentException e) {
                    throw new InvalidFileFormatException("Unable to read status");
                }
                break;
            default :
                throw new InvalidFileFormatException(
                        JsonReaderWriter.ERROR_UNKNOWN_ELEMENT + key);
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
                throw new InvalidFileFormatException(
                        JsonReaderWriter.ERROR_UNKNOWN_ELEMENT + key);
        }
    }

    private static JsonObjectBuilder createJsonObjectBuilder(
            TaskInfo taskInfo) {
        
        JsonObjectBuilder builder = Json.createObjectBuilder();
        
        builder.add(JSON_NAME,
                JsonItemParser.stringToJsonString(taskInfo.name));
        builder.add(JSON_START_TIME,
                JsonItemParser.localTimeToString(taskInfo.startTime));
        builder.add(JSON_START_DATE,
                JsonItemParser.localDateToString(taskInfo.startDate));
        builder.add(JSON_END_TIME,
                JsonItemParser.localTimeToString(taskInfo.endTime));
        builder.add(JSON_END_DATE,
                JsonItemParser.localDateToString(taskInfo.endDate));
        builder.add(JSON_DETAILS,
                JsonItemParser.stringToJsonString(taskInfo.details));
        
        builderAddTags(taskInfo.tags, builder);
        
        builder.add(JSON_PRIORITY,
                JsonItemParser.priorityToString(taskInfo.priority));
        builder.add(JSON_STATUS,
                JsonItemParser.statusToString(taskInfo.status));
        
        // PENDING IMPLEMENTATION
        //builder.add(JSON_NUMBER_OF_TIMES, taskInfo.numberOfTimes);
        //builder.add(JSON_REPEAT_INTERVAL, taskInfo.repeatInterval);
        
        return builder;
    }


    private static void builderAddTags(Tag[] tags, JsonObjectBuilder builder) {
        if (tags == null) {
            builder.add(JSON_TAGS, JsonItemParser.STRING_NULL);
            
        } else {
            JsonArrayBuilder tagArrayJson = Json.createArrayBuilder();
            for (Tag tag : tags) {
                tagArrayJson.add(tag.toString());
            }
            
            builder.add(JSON_TAGS, tagArrayJson);
        }
    }
}
