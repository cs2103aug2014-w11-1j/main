package main.formatting.test;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import main.formatting.ReportFormatter;
import main.message.ReportMessage;

import org.junit.Test;

import data.taskinfo.Priority;
import data.taskinfo.TaskInfo;

//@author A0113011L
public class ReportFormatterTest {

    @Test
    public void test() {
        ArrayList<TaskInfo> tasks = new ArrayList<TaskInfo>();
        TaskInfo task1 = TaskInfo.create();
        task1.endDate = LocalDate.parse("2011-12-03");
        task1.endTime = LocalTime.parse("10:50");
        task1.name = "abcd";
        task1.priority = Priority.HIGH;
        tasks.add(task1);
        
        TaskInfo task2 = TaskInfo.create();
        task2.endDate = LocalDate.parse("2011-12-03");
        task2.endTime = LocalTime.parse("13:00");
        task2.name = "efgh";
        task2.priority = Priority.HIGH;
        tasks.add(task2);
        
        TaskInfo task3 = TaskInfo.create();
        task3.endDate = LocalDate.parse("2011-12-04");
        task3.endTime = LocalTime.parse("12:00");
        task3.name = "ijkl";
        task3.priority = Priority.HIGH;
        tasks.add(task3);
        
        ReportMessage message = new ReportMessage(5, 3, tasks);
        
        String expected = "You have 5 tasks today, and 3 tasks tomorrow." + System.lineSeparator() +
                "Below are the high-priority tasks." + System.lineSeparator() +
                "Sat, 3 Dec 2011 ---" + System.lineSeparator() +
                "1) [   10:50   ] abcd                                                          " + System.lineSeparator() +
                "2) [   13:00   ] efgh                                                          " + System.lineSeparator() +
                "Sun, 4 Dec 2011 ---" + System.lineSeparator() +
                "3) [   12:00   ] ijkl                                                          " + System.lineSeparator();
        
        ReportFormatter formatter = new ReportFormatter();
        assertEquals(expected, formatter.format(message));
    }

}
