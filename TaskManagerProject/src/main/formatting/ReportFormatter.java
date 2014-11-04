package main.formatting;

import main.formatting.utility.SummaryUtility;
import main.message.ReportMessage;
import data.taskinfo.TaskInfo;


//@author A0113011L
public class ReportFormatter {
    private final static String HEADER_REPORT = "You have %1$s tasks today, "
            + "and %2$s tasks tomorrow." + System.lineSeparator() + 
            "Below are the high-priority tasks." + System.lineSeparator();
    SummaryUtility summaryUtility;
    public ReportFormatter() {
        summaryUtility = new SummaryUtility();
    }
    
    public String format(ReportMessage message) {
        StringBuilder result = new StringBuilder();
        result.append(String.format(HEADER_REPORT, 
                message.getCountTodayTask(), message.getCountTmrTask()));
        TaskInfo[] tasks = new TaskInfo[message.getUrgentTask().size()];
        message.getUrgentTask().toArray(tasks);
        result.append(summaryUtility.format(tasks, null));
        return result.toString() + System.lineSeparator();
    }
}
