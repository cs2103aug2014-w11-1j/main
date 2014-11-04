package main.formatting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import main.formatting.utility.SummaryUtility;
import main.message.ReportMessage;
import data.taskinfo.TaskInfo;


//@author A0113011L
public class ReportFormatter {
    private final static String FORMAT_DATE = "Date: %1$s";
    private final static String DATETIME_FORMAT_DATE = "EEEE, d MMM Y";
    private final static String FORMAT_TIME = "Time: %1$s";
    private final static String DATETIME_FORMAT_TIME = "HH:mm (a)";
    
    private final static String DATETIME_REPORT = "[ %1$s, %2$s ]" +
            System.lineSeparator();
    private final static String HEADER_REPORT = "You have %1$s tasks today, " +
            "and %2$s tasks tomorrow." + System.lineSeparator() + 
            "Below are the high-priority tasks." + System.lineSeparator();
    
    SummaryUtility summaryUtility;
    public ReportFormatter() {
        summaryUtility = new SummaryUtility();
    }
    
    public String format(ReportMessage message) {
        StringBuilder result = new StringBuilder();
        result.append(String.format(DATETIME_REPORT, currentDate(),
                currentTime()));
        result.append(String.format(HEADER_REPORT, 
                message.getCountTodayTask(), message.getCountTmrTask()));
        TaskInfo[] tasks = new TaskInfo[message.getUrgentTask().size()];
        message.getUrgentTask().toArray(tasks);
        result.append(summaryUtility.format(tasks, null));
        return result.toString();
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
