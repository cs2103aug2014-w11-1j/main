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
        //testFileHash();
        //testDateTimeConversion();
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
        
        TaskInfo[] taskInfos = new TaskInfo[1];
        taskInfos[0] = taskInfo;

        String json = FileInputOutput.tasksToJson(taskInfos);
        System.out.println(json);
    }

    private void testDateTimeConversion() {
        Duration duration;
        String durationString;

        duration = Duration.ofHours(15);
        durationString = FileInputOutput.durationToString(duration);
        assertEquals(duration, FileInputOutput.stringToDuration(durationString));
        
        duration = Duration.ofDays(10000);
        durationString = FileInputOutput.durationToString(duration);
        assertEquals(duration, FileInputOutput.stringToDuration(durationString));
        
        duration = Duration.ofSeconds(3);
        durationString = FileInputOutput.durationToString(duration);
        assertEquals(duration, FileInputOutput.stringToDuration(durationString));
        
        duration = Duration.ofSeconds(1200);
        durationString = FileInputOutput.durationToString(duration);
        assertEquals(duration, FileInputOutput.stringToDuration(durationString));
        
        duration = Duration.ofHours(0);
        durationString = FileInputOutput.durationToString(duration);
        assertEquals(duration, FileInputOutput.stringToDuration(durationString));
        
        duration = Duration.ofHours(24);
        durationString = FileInputOutput.durationToString(duration);
        assertEquals(duration, FileInputOutput.stringToDuration(durationString));

        durationString = "test";
        assertEquals(null, FileInputOutput.stringToDuration(durationString));

        LocalTime time;
        String timeString;
        
        time = LocalTime.of(5, 4, 3);
        timeString = FileInputOutput.localTimeToString(time);
        assertEquals(time, FileInputOutput.stringToLocalTime(timeString));
        
        time = LocalTime.of(0, 1, 1);
        timeString = FileInputOutput.localTimeToString(time);
        assertEquals(time, FileInputOutput.stringToLocalTime(timeString));
        
        time = LocalTime.of(1, 0, 1);
        timeString = FileInputOutput.localTimeToString(time);
        assertEquals(time, FileInputOutput.stringToLocalTime(timeString));
        
        time = LocalTime.of(1, 1, 0);
        timeString = FileInputOutput.localTimeToString(time);
        assertEquals(time, FileInputOutput.stringToLocalTime(timeString));
        
        time = LocalTime.of(0, 0, 0);
        timeString = FileInputOutput.localTimeToString(time);
        assertEquals(time, FileInputOutput.stringToLocalTime(timeString));
        
        time = LocalTime.of(23, 59, 59);
        timeString = FileInputOutput.localTimeToString(time);
        assertEquals(time, FileInputOutput.stringToLocalTime(timeString));
        
        time = LocalTime.of(11, 10, 3);
        timeString = FileInputOutput.localTimeToString(time);
        assertEquals(time, FileInputOutput.stringToLocalTime(timeString));

        LocalDate date;
        String dateString;
        
        date = LocalDate.of(2013, 12, 31);
        dateString = FileInputOutput.localDateToString(date);
        assertEquals(date, FileInputOutput.stringToLocalDate(dateString));
        
        date = LocalDate.of(1990, 2, 2);
        dateString = FileInputOutput.localDateToString(date);
        assertEquals(date, FileInputOutput.stringToLocalDate(dateString));
        
        date = LocalDate.of(2012, 2, 29);
        dateString = FileInputOutput.localDateToString(date);
        assertEquals(date, FileInputOutput.stringToLocalDate(dateString));
        
        date = LocalDate.of(1, 1, 1);
        dateString = FileInputOutput.localDateToString(date);
        assertEquals(date, FileInputOutput.stringToLocalDate(dateString));

        date = LocalDate.of(3012, 7, 4);
        dateString = FileInputOutput.localDateToString(date);
        assertEquals(date, FileInputOutput.stringToLocalDate(dateString));

        date = LocalDate.of(1000, 7, 7);
        dateString = FileInputOutput.localDateToString(date);
        assertEquals(date, FileInputOutput.stringToLocalDate(dateString));

        date = LocalDate.now();
        dateString = FileInputOutput.localDateToString(date);
        assertEquals(date, FileInputOutput.stringToLocalDate(dateString));
        
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
