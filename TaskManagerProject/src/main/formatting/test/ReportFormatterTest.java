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

        ArrayList<TaskInfo> nonUrgentTasks = new ArrayList<TaskInfo>();

        ArrayList<TaskInfo> missedTasks = new ArrayList<TaskInfo>();
        TaskInfo task4 = TaskInfo.create();
        task4.endDate = LocalDate.parse("2011-11-04");
        task4.endTime = LocalTime.parse("08:42");
        task4.name = "mnop";
        missedTasks.add(task4);
        
        ReportMessage message = new ReportMessage(5, 3, tasks, nonUrgentTasks,
                missedTasks);

        ReportFormatter formatter = new ReportFormatter();
        String result = removeFirstLine(formatter.format(message));
        
        String expected = "You have 5 tasks today, and 3 tasks tomorrow." + System.lineSeparator() +
                "Overdue tasks:" + System.lineSeparator() +
                "Fri, 4 Nov 2011 ---" + System.lineSeparator() +
                "   [   08:42   ] \u001b[31mmnop                                                   \u001b[0m       " + System.lineSeparator() + System.lineSeparator() +
                "High-priority tasks:" + System.lineSeparator() +
                "Sat, 3 Dec 2011 ---" + System.lineSeparator() +
                "   [   10:50   ] \u001b[31mabcd                                                   \u001b[0m       " + System.lineSeparator() +
                "   [   13:00   ] \u001b[31mefgh                                                   \u001b[0m       " + System.lineSeparator() + System.lineSeparator() +
                "Sun, 4 Dec 2011 ---" + System.lineSeparator() +
                "   [   12:00   ] \u001b[31mijkl                                                   \u001b[0m       " + System.lineSeparator() + System.lineSeparator();
        
        assertEquals(expected, result);
    }

    @Test
    public void testEmpty() {
        ArrayList<TaskInfo> noTasks = new ArrayList<>();
        ReportMessage message = new ReportMessage(0, 0, noTasks, noTasks,
                noTasks);

        ReportFormatter formatter = new ReportFormatter();
        String result = removeFirstLine(formatter.format(message));
        
        String expected = "You have 0 tasks today, and 0 tasks tomorrow." + System.lineSeparator() +
                "Congratulations! You have no pending tasks." + System.lineSeparator() + System.lineSeparator();
        
        assertEquals(expected, result);
    }

    private String removeFirstLine(String result) {
        String[] split = result.split(System.lineSeparator(), 2);
        return split[1];
    }

}
