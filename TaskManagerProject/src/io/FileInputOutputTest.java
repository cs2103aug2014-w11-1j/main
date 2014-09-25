package io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.Test;

import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class FileInputOutputTest {

    @Test
    public void test() {
        testFileHash();
        testDateTimeConversion();
        testJsonParsing();
    }
    
    private void testJsonParsing() {
        TaskInfo taskInfo = new TaskInfo();
        taskInfo.name = "A little boy going to the market";
        taskInfo.details = "The market is very big.";
        taskInfo.duration = Duration.ofHours(2);
        taskInfo.endDate = LocalDate.now();
        taskInfo.endTime = LocalTime.of(14, 0, 0);
        taskInfo.priority = Priority.MEDIUM;
        taskInfo.status = Status.DONE;
        taskInfo.tags = new Tag[]{new Tag("boy"), new Tag("market")};

        TaskInfo taskInfo2 = new TaskInfo();
        taskInfo2.name = "kasjfklaf\n\n\n^%#^#$%@%lkA<FZ>>>>???>></////||||||()()()%%%DDDD\r\n";
        taskInfo2.details = null;
        taskInfo2.duration = null;
        taskInfo2.endDate = null;
        taskInfo2.endTime = null;
        taskInfo2.priority = null;
        taskInfo2.status = null;
        taskInfo2.tags = null;

        TaskInfo taskInfo3 = new TaskInfo();
        taskInfo2.name = "";
        taskInfo2.details = "";
        taskInfo2.duration = null;
        taskInfo.endDate = LocalDate.of(160, 2, 29);
        taskInfo2.endTime = null;
        taskInfo2.priority = null;
        taskInfo2.status = null;
        taskInfo2.tags = new Tag[0];

        TaskInfo taskInfo4 = new TaskInfo();
        taskInfo.name = "null";
        taskInfo.details = "null";
        taskInfo.duration = Duration.ofHours(2);
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
        String json = JsonFileFormatter.tasksToJsonString(taskInfos);

        TaskInfo[] loadTaskInfos = null;
        try {
            loadTaskInfos = JsonFileFormatter.jsonStringToTasks(json);
        } catch (InvalidFileFormatException e) {
            e.printStackTrace();
        }

        assertEquals(taskInfos.length, loadTaskInfos.length);
        for (int i = 0; i < taskInfos.length; i++) {
            assertEquals(taskInfos[i], loadTaskInfos[i]);
        }
        
        
        String json2 = JsonFileFormatter.tasksToJsonString(loadTaskInfos);
        assertEquals(json, json2);
    }

    private void testDateTimeConversion() {
        Duration duration;
        String durationString;

        duration = Duration.ofHours(15);
        durationString = JsonFileFormatter.durationToString(duration);
        assertEquals(duration, JsonFileFormatter.stringToDuration(durationString));
        
        duration = Duration.ofDays(10000);
        durationString = JsonFileFormatter.durationToString(duration);
        assertEquals(duration, JsonFileFormatter.stringToDuration(durationString));
        
        duration = Duration.ofSeconds(3);
        durationString = JsonFileFormatter.durationToString(duration);
        assertEquals(duration, JsonFileFormatter.stringToDuration(durationString));
        
        duration = Duration.ofSeconds(1200);
        durationString = JsonFileFormatter.durationToString(duration);
        assertEquals(duration, JsonFileFormatter.stringToDuration(durationString));
        
        duration = Duration.ofHours(0);
        durationString = JsonFileFormatter.durationToString(duration);
        assertEquals(duration, JsonFileFormatter.stringToDuration(durationString));
        
        duration = Duration.ofHours(24);
        durationString = JsonFileFormatter.durationToString(duration);
        assertEquals(duration, JsonFileFormatter.stringToDuration(durationString));

        durationString = "test";
        assertEquals(null, JsonFileFormatter.stringToDuration(durationString));

        LocalTime time;
        String timeString;
        
        time = LocalTime.of(5, 4, 3);
        timeString = JsonFileFormatter.localTimeToString(time);
        assertEquals(time, JsonFileFormatter.stringToLocalTime(timeString));
        
        time = LocalTime.of(0, 1, 1);
        timeString = JsonFileFormatter.localTimeToString(time);
        assertEquals(time, JsonFileFormatter.stringToLocalTime(timeString));
        
        time = LocalTime.of(1, 0, 1);
        timeString = JsonFileFormatter.localTimeToString(time);
        assertEquals(time, JsonFileFormatter.stringToLocalTime(timeString));
        
        time = LocalTime.of(1, 1, 0);
        timeString = JsonFileFormatter.localTimeToString(time);
        assertEquals(time, JsonFileFormatter.stringToLocalTime(timeString));
        
        time = LocalTime.of(0, 0, 0);
        timeString = JsonFileFormatter.localTimeToString(time);
        assertEquals(time, JsonFileFormatter.stringToLocalTime(timeString));
        
        time = LocalTime.of(23, 59, 59);
        timeString = JsonFileFormatter.localTimeToString(time);
        assertEquals(time, JsonFileFormatter.stringToLocalTime(timeString));
        
        time = LocalTime.of(11, 10, 3);
        timeString = JsonFileFormatter.localTimeToString(time);
        assertEquals(time, JsonFileFormatter.stringToLocalTime(timeString));

        LocalDate date;
        String dateString;
        
        date = LocalDate.of(2013, 12, 31);
        dateString = JsonFileFormatter.localDateToString(date);
        assertEquals(date, JsonFileFormatter.stringToLocalDate(dateString));
        
        date = LocalDate.of(1990, 2, 2);
        dateString = JsonFileFormatter.localDateToString(date);
        assertEquals(date, JsonFileFormatter.stringToLocalDate(dateString));
        
        date = LocalDate.of(2012, 2, 29);
        dateString = JsonFileFormatter.localDateToString(date);
        assertEquals(date, JsonFileFormatter.stringToLocalDate(dateString));
        
        date = LocalDate.of(1, 1, 1);
        dateString = JsonFileFormatter.localDateToString(date);
        assertEquals(date, JsonFileFormatter.stringToLocalDate(dateString));

        date = LocalDate.of(3012, 7, 4);
        dateString = JsonFileFormatter.localDateToString(date);
        assertEquals(date, JsonFileFormatter.stringToLocalDate(dateString));

        date = LocalDate.of(1000, 7, 7);
        dateString = JsonFileFormatter.localDateToString(date);
        assertEquals(date, JsonFileFormatter.stringToLocalDate(dateString));

        date = LocalDate.now();
        dateString = JsonFileFormatter.localDateToString(date);
        assertEquals(date, JsonFileFormatter.stringToLocalDate(dateString));
        
    }

    private void testFileHash() {
        String message = "Hello World! This is a test file!";
        
        try {
            FileWriter writer = new FileWriter("testFile.txt");
            writer.write(message);
            writer.flush();
            writer.close();
        
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileWriter writer = new FileWriter("fileTest.txt");
            writer.write("THIS IS A WRONG TEST FILE!");
            writer.flush();
            writer.close();
        
        } catch (IOException e) {
            e.printStackTrace();
        }

        String correctHash = "6bd6955dce300739cdea07fa4fc574aa";
        
        String hash1 = FileInputOutput.computeHash("testFile.txt");
        String hash2 = FileInputOutput.computeHash("fileTest.txt");
        
        assertEquals(correctHash, hash1);
        assertEquals(correctHash.length(), hash2.length());
        assertFalse(correctHash.equals(hash2));
    }

}
