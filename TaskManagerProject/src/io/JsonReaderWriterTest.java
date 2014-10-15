package io;

import static org.junit.Assert.assertEquals;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class JsonReaderWriterTest {

    @Test
    public void test() {
        testDateTimeConversion();
        testJsonParsing();
    }
    
    
    private void testJsonParsing() {
        TaskInfo taskInfo = TaskInfo.create();
        taskInfo.name = "A little boy going to the market";
        taskInfo.details = "The market is very big.";
        taskInfo.startDate = LocalDate.of(1999, 8, 15);
        taskInfo.startTime = LocalTime.of(11, 12, 0);
        taskInfo.endDate = LocalDate.now();
        taskInfo.endTime = LocalTime.of(14, 0, 0);
        taskInfo.priority = Priority.MEDIUM;
        taskInfo.status = Status.DONE;
        taskInfo.tags = new Tag[]{new Tag("boy"), new Tag("market")};

        TaskInfo taskInfo2 = TaskInfo.create();
        taskInfo2.name = "kasjfklaf\n\n\n^%#^#$%@%lkA<FZ>>>>???>></////||||||()()()%%%DDDD\r\n";
        taskInfo2.details = null;
        taskInfo.startDate = null;
        taskInfo.startTime = null;
        taskInfo2.endDate = null;
        taskInfo2.endTime = null;
        taskInfo2.priority = null;
        taskInfo2.status = null;
        taskInfo2.tags = null;

        TaskInfo taskInfo3 = TaskInfo.create();
        taskInfo2.name = "";
        taskInfo2.details = "";
        taskInfo.startDate = null;
        taskInfo.startTime = LocalTime.of(0, 15, 16);
        taskInfo.endDate = LocalDate.of(160, 2, 29);
        taskInfo2.endTime = null;
        taskInfo2.priority = Priority.NONE;
        taskInfo2.status = Status.UNDONE;
        taskInfo2.tags = new Tag[0];

        TaskInfo taskInfo4 = TaskInfo.create();
        taskInfo.name = "null";
        taskInfo.details = "null";
        taskInfo.endDate = LocalDate.of(2, 2, 2);
        taskInfo.endTime = LocalTime.of(0, 0, 0);
        taskInfo.endDate = LocalDate.of(1, 1, 1);
        taskInfo.endTime = LocalTime.of(0, 59, 59);
        taskInfo.priority = null;
        taskInfo.status = Status.UNDONE;
        taskInfo.tags = new Tag[]{new Tag("boy"), new Tag("market"),
                new Tag("market"), new Tag("market"), new Tag("market"),
                new Tag("market"), new Tag("boy"), new Tag("market"),
                new Tag("market")};
        
        
        
        TaskInfo[] taskInfos = new TaskInfo[2];
        taskInfos[0] = taskInfo;
        taskInfos[1] = taskInfo2;
        testJsonParsing(taskInfos);

        
        taskInfos = new TaskInfo[0];
        testJsonParsing(taskInfos);
        

        taskInfos = new TaskInfo[20];
        for (int i = 0; i < 5; i++) {
            taskInfos[4 * i + 0] = taskInfo;
            taskInfos[4 * i + 1] = taskInfo2;
            taskInfos[4 * i + 2] = taskInfo3;
            taskInfos[4 * i + 3] = taskInfo4;
        }
        
        testJsonParsing(taskInfos);
    }

    private void testJsonParsing(TaskInfo[] taskInfos) {
        String json = JsonReaderWriter.tasksToJsonString(taskInfos);

        TaskInfo[] expectedResult = createExpectedResult(taskInfos);
        TaskInfo[] loadTaskInfos = null;
        
        try {
            loadTaskInfos = JsonReaderWriter.jsonStringToTasks(json);
        } catch (InvalidFileFormatException e) {
            e.printStackTrace();
        }

        assertEquals(taskInfos.length, loadTaskInfos.length);
        
        for (int i = 0; i < taskInfos.length; i++) {
            assertEquals(expectedResult[i], loadTaskInfos[i]);
        }
        
        String json2 = JsonReaderWriter.tasksToJsonString(loadTaskInfos);
        assertEquals(json, json2);
    }

    private void testDateTimeConversion() {
        Duration duration;
        String durationString;

        duration = Duration.ofHours(15);
        durationString = JsonReaderWriter.durationToString(duration);
        assertEquals(duration, JsonReaderWriter.stringToDuration(durationString));
        
        duration = Duration.ofDays(10000);
        durationString = JsonReaderWriter.durationToString(duration);
        assertEquals(duration, JsonReaderWriter.stringToDuration(durationString));
        
        duration = Duration.ofSeconds(3);
        durationString = JsonReaderWriter.durationToString(duration);
        assertEquals(duration, JsonReaderWriter.stringToDuration(durationString));
        
        duration = Duration.ofSeconds(1200);
        durationString = JsonReaderWriter.durationToString(duration);
        assertEquals(duration, JsonReaderWriter.stringToDuration(durationString));
        
        duration = Duration.ofHours(0);
        durationString = JsonReaderWriter.durationToString(duration);
        assertEquals(duration, JsonReaderWriter.stringToDuration(durationString));
        
        duration = Duration.ofHours(24);
        durationString = JsonReaderWriter.durationToString(duration);
        assertEquals(duration, JsonReaderWriter.stringToDuration(durationString));

        durationString = "test";
        assertEquals(null, JsonReaderWriter.stringToDuration(durationString));

        LocalTime time;
        String timeString;
        
        time = LocalTime.of(5, 4, 3);
        timeString = JsonReaderWriter.localTimeToString(time);
        assertEquals(time, JsonReaderWriter.stringToLocalTime(timeString));
        
        time = LocalTime.of(0, 1, 1);
        timeString = JsonReaderWriter.localTimeToString(time);
        assertEquals(time, JsonReaderWriter.stringToLocalTime(timeString));
        
        time = LocalTime.of(1, 0, 1);
        timeString = JsonReaderWriter.localTimeToString(time);
        assertEquals(time, JsonReaderWriter.stringToLocalTime(timeString));
        
        time = LocalTime.of(1, 1, 0);
        timeString = JsonReaderWriter.localTimeToString(time);
        assertEquals(time, JsonReaderWriter.stringToLocalTime(timeString));
        
        time = LocalTime.of(0, 0, 0);
        timeString = JsonReaderWriter.localTimeToString(time);
        assertEquals(time, JsonReaderWriter.stringToLocalTime(timeString));
        
        time = LocalTime.of(23, 59, 59);
        timeString = JsonReaderWriter.localTimeToString(time);
        assertEquals(time, JsonReaderWriter.stringToLocalTime(timeString));
        
        time = LocalTime.of(11, 10, 3);
        timeString = JsonReaderWriter.localTimeToString(time);
        assertEquals(time, JsonReaderWriter.stringToLocalTime(timeString));

        LocalDate date;
        String dateString;
        
        date = LocalDate.of(2013, 12, 31);
        dateString = JsonReaderWriter.localDateToString(date);
        assertEquals(date, JsonReaderWriter.stringToLocalDate(dateString));
        
        date = LocalDate.of(1990, 2, 2);
        dateString = JsonReaderWriter.localDateToString(date);
        assertEquals(date, JsonReaderWriter.stringToLocalDate(dateString));
        
        date = LocalDate.of(2012, 2, 29);
        dateString = JsonReaderWriter.localDateToString(date);
        assertEquals(date, JsonReaderWriter.stringToLocalDate(dateString));
        
        date = LocalDate.of(1, 1, 1);
        dateString = JsonReaderWriter.localDateToString(date);
        assertEquals(date, JsonReaderWriter.stringToLocalDate(dateString));

        date = LocalDate.of(3012, 7, 4);
        dateString = JsonReaderWriter.localDateToString(date);
        assertEquals(date, JsonReaderWriter.stringToLocalDate(dateString));

        date = LocalDate.of(1000, 7, 7);
        dateString = JsonReaderWriter.localDateToString(date);
        assertEquals(date, JsonReaderWriter.stringToLocalDate(dateString));

        date = LocalDate.now();
        dateString = JsonReaderWriter.localDateToString(date);
        assertEquals(date, JsonReaderWriter.stringToLocalDate(dateString));
    }
    
    private TaskInfo[] createExpectedResult(TaskInfo[] taskInfos) {
        TaskInfo[] expectedResult = new TaskInfo[taskInfos.length];
        
        for (int i = 0; i < taskInfos.length; i++) {
            expectedResult[i] = new TaskInfo(taskInfos[i]);
            
            if (expectedResult[i].status == null) {
                expectedResult[i].status = Status.defaultStatus();
            }
            if (expectedResult[i].priority == null) {
                expectedResult[i].priority = Priority.defaultPriority();
            }
        }
        return expectedResult;
    }

}
