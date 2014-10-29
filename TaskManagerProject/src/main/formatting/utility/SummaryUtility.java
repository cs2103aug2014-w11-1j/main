package main.formatting.utility;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class SummaryUtility {
    private final static int WIDTH_LINE = 79;
    private final static int WIDTH_TIME = 14;
    private final static int WIDTH_ABSOLUTE = 7;
    private final static String LINE_FLOATING = "Floating Tasks ---";
    private final static String LINE_NO_TASK = "No tasks found.";

    
    private String getDateLine(TaskInfo task) {
        LocalDate date;
        date = task.endDate;
        if (task.startDate != null && task.endTime == LocalTime.MIDNIGHT){
            date = task.endDate.plusDays(-1);
        }
        DateTimeFormatter formatter = 
                DateTimeFormatter.ofPattern("E, d MMM u");
        String dateString = formatter.format(date);
        return dateString + " ---";
    }
    
    private String getTimeString(LocalTime startTime, LocalTime endTime) {
        if(startTime == null && endTime == null) {
            return "              ";
        } else if (startTime == null) {
            DateTimeFormatter formatter = 
                    DateTimeFormatter.ofPattern("HH:mm");
            String addedFormat = "[   %1$s   ] ";
            return String.format(addedFormat, formatter.format(endTime));
        } else {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("HH:mm");
            String addedFormat = "[%1$s-%2$s] ";
            String startTimeString = formatter.format(startTime);
            String endTimeString = formatter.format(endTime);
            if (endTimeString.equals("00:00")) {
                endTimeString = "24:00";
            }
            return String.format(addedFormat, startTimeString, endTimeString);
        }
    }
    
    int numberLength(int number) {
        String numberString = Integer.toString(number);
        return numberString.length();
    }
    
    private String cutToWidth(String s, int width) {
        String cutString = s.substring(0, width - 3);
        return cutString + "...";
    }
    
    private String padRightToWidth(String s, int width) {
        StringBuilder builder = new StringBuilder(s);
        for (int i = s.length(); i < width; i++) {
            builder.append(" ");
        }
        return builder.toString();
    }
    
    
    private String padLeftToWidth(String s, int width) {
        StringBuilder builder = new StringBuilder("");
        for (int i = s.length(); i < width; i++) {
            builder.append(" ");
        }
        builder.append(s);
        return builder.toString();
    }
    
    private String getTaskNameString(String taskName, int width) {
        if (taskName.length() > width) {
            return cutToWidth(taskName, width);
        } else {
            return padRightToWidth(taskName, width);
        }
    }
    
    private String getTaskNumberString(int taskNumber, int width) {
        String line = Integer.toString(taskNumber) + ") ";
        return padLeftToWidth(line, width);
    }
    
    private String getAbsoluteTaskIdString(TaskId taskId) {
        if (taskId == null) {
            return "       ";
        } else {
            return String.format("- [%1$s]", taskId.toString());
        }
    }
    
    private String getTaskInfoLine(TaskInfo task, TaskId taskId, 
            int taskNumber, 
            int numberWidth) {
        StringBuilder line = new StringBuilder();
        int taskNameWidth = WIDTH_LINE - WIDTH_ABSOLUTE - WIDTH_TIME - 
                numberWidth;
        line.append(getTaskNumberString(taskNumber, numberWidth));
        line.append(getTimeString(task.startTime, task.endTime));
        line.append(getTaskNameString(task.name, taskNameWidth));
        line.append(getAbsoluteTaskIdString(taskId));
        return line.toString();
    }
    
    private String getFloatingTaskLine() {
        return LINE_FLOATING;
    }
    
    private boolean isEndingAtMidnight(TaskInfo task) {
        LocalTime midnight = LocalTime.parse("00:00");
        return task.startTime != null && task.endTime.equals(midnight);
    }
    
    private boolean isOneDayAfter(TaskInfo taskBefore, TaskInfo taskAfter) {
        return taskBefore.endDate.plusDays(1).equals(taskAfter.endDate);
    }
    
    private LocalDate getActualDate(TaskInfo task) {
        if (task.endTime == null) {
            return task.endDate;
        } else if (task.startTime == null) {
            return task.endDate;
        } else {
            if (task.endTime == LocalTime.MIDNIGHT) {
                return task.endDate.plusDays(-1);
            } else {
                return task.endDate;
            }
        }
    }
    
    private boolean isDifferentDate(TaskInfo task1, TaskInfo task2) {
        LocalDate date1 = getActualDate(task1);
        LocalDate date2 = getActualDate(task2);
        if (date1 == null) {
            return date2 != null;
        } else {
            return !date1.equals(date2);
        }
    }
    
    private ArrayList<String> formatToArrayList(TaskInfo[] tasks, 
            TaskId[] taskIds) {
        ArrayList<String> result = new ArrayList<String>();
        if (tasks.length == 0)
            result.add(LINE_NO_TASK);
        else {
            int numberWidth = numberLength(tasks.length) + 2;
            for (int i = 0; i < tasks.length; i++) {
                if (tasks[i].endDate == null) {
                    if (i == 0 || tasks[i-1].endDate != null) {
                        result.add(getFloatingTaskLine());
                    }
                }
                else {
                    if (i == 0 || isDifferentDate(tasks[i], tasks[i-1])) {
                        result.add(getDateLine(tasks[i]));
                    }
                }
                if (taskIds == null) {
                    result.add(getTaskInfoLine(tasks[i], null, 
                            i + 1 , numberWidth));
                } else {
                    result.add(getTaskInfoLine(tasks[i], taskIds[i], 
                            i + 1, numberWidth));
                }
            }
        }
        return result;
        
    }
    
    private String arrayListToStringLines(ArrayList<String> lines) {
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            result.append(line);
            result.append(System.lineSeparator());
        }
        return result.toString();
    }
    
    public String format(TaskInfo[] tasks, TaskId[] taskIds) {
        ArrayList<String> formattedTaskArray = 
                formatToArrayList(tasks, taskIds);
        return arrayListToStringLines(formattedTaskArray);
    }
}
