package main.formatting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import main.formatting.utility.SummaryUtility;
import main.message.ReportMessage;
import data.taskinfo.TaskInfo;


/**
 * Formatter for the ReportMessage class.
 * Example:
 * [  Sunday, 9 Nov 2014, 17:17 (PM) ]
 * You have 2 tasks today, and 1 tasks tomorrow.
 * Overdue tasks:
 * Fri, 7 Nov 2014 ---
 *   [   11:00   ] Task1
 *   [   17:00   ] Task2
 *   
 * High-priority tasks:
 * Sun, 9 Nov 2014 ---
 *   [12:00-14:00] Task3
 * 
 * Non-urgent tasks:
 * Sun, 9 Nov 2014
 *   [   15:00   ] Task4
 *   
 * @author Nathan
 *
 */
//@author A0113011L
public class ReportFormatter {
    private final static String DATETIME_FORMAT_DATE = "EEEE, d MMM Y";
    private final static String DATETIME_FORMAT_TIME = "HH:mm (a)";
    
    private final static String REPORT_DATETIME = "[ %1$s, %2$s ]" +
            System.lineSeparator();
    private final static String REPORT_HEADER = "You have %1$s tasks today, " +
            "and %2$s tasks tomorrow." + System.lineSeparator();

    private final static String REPORT_MISSED =
            "Overdue tasks:" + System.lineSeparator();
    private final static String REPORT_URGENT =
            "High-priority tasks:" + System.lineSeparator();
    private final static String REPORT_NON_URGENT =
            "Non-urgent tasks:" + System.lineSeparator();
    private final static String REPORT_NO_TASKS =
            "Congratulations! You have no pending tasks." + System.lineSeparator();
    
    SummaryUtility summaryUtility;
    public ReportFormatter() {
        summaryUtility = new SummaryUtility();
    }
    
    /**
     * Format the ReportMessage to a String.
     * @param message The message to be formatted.
     * @return The formatting result.
     */
    public String format(ReportMessage message) {
        StringBuilder result = new StringBuilder();
        result.append(String.format(REPORT_DATETIME, currentDate(),
                currentTime()));
        result.append(String.format(REPORT_HEADER, 
                message.getCountTodayTask(), message.getCountTmrTask()));

        TaskInfo[] urgentTasks = retrieveUrgentTasks(message);
        TaskInfo[] nonUrgentTasks = retrieveNonUrgentTasks(message);
        TaskInfo[] missedTasks = retrieveMissedTasks(message);

        if (missedTasks.length != 0) {
            result.append(REPORT_MISSED);
            result.append(summaryUtility.format(missedTasks, null, false));
            result.append(System.lineSeparator());
        }

        if (urgentTasks.length != 0) {
            result.append(REPORT_URGENT);
            result.append(summaryUtility.format(urgentTasks, null, false));
            result.append(System.lineSeparator());
        }

        if (nonUrgentTasks.length != 0) {
            result.append(REPORT_NON_URGENT);
            result.append(summaryUtility.format(nonUrgentTasks, null, false));
            result.append(System.lineSeparator());
        }
        
        if (missedTasks.length == 0 && urgentTasks.length == 0 &&
                nonUrgentTasks.length == 0) {
            result.append(REPORT_NO_TASKS);
            result.append(System.lineSeparator());
        }
        
        return result.toString();
    }

    private TaskInfo[] retrieveUrgentTasks(ReportMessage message) {
        ArrayList<TaskInfo> taskList = message.getUrgentTasks();
        TaskInfo[] tasks = new TaskInfo[taskList.size()];
        taskList.toArray(tasks);
        return tasks;
    }

    private TaskInfo[] retrieveNonUrgentTasks(ReportMessage message) {
        ArrayList<TaskInfo> taskList = message.getNonUrgentTasks();
        TaskInfo[] tasks = new TaskInfo[taskList.size()];
        taskList.toArray(tasks);
        return tasks;
    }

    private TaskInfo[] retrieveMissedTasks(ReportMessage message) {
        ArrayList<TaskInfo> taskList = message.getMissedTasks();
        TaskInfo[] tasks = new TaskInfo[taskList.size()];
        taskList.toArray(tasks);
        return tasks;
    }
    
    
    private String currentTime() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(DATETIME_FORMAT_TIME);
        return formatter.format(LocalTime.now());
    }

    private String currentDate() {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern(DATETIME_FORMAT_DATE);
        return formatter.format(LocalDate.now());
    }
}
