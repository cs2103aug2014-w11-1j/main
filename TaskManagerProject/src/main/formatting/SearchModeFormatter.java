package main.formatting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import main.modeinfo.SearchModeInfo;
import data.taskinfo.TaskInfo;

public class SearchModeFormatter {
    private final static int WIDTH_LINE = 80;
    private final static int WIDTH_TIME = 13;
    private final static int WIDTH_ABSOLUTE = 7;
    
    public void sortTask(TaskInfo[] tasks) {
        Arrays.sort(tasks, new Comparator<TaskInfo>() {
            public int compare(TaskInfo task1, TaskInfo task2) {
                if (task1.endDate.compareTo(task2.endDate) < 0) {
                    return -1;
                } else if (task1.endDate.compareTo(task2.endDate) > 0) {
                    return 1;
                } else {
                    return task1.endTime.compareTo(task2.endTime);
                }
            }
        });
    }
    
    private String getDateLine(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E, d MMM u");
        String dateString = formatter.format(date);
        return dateString + " ---";
    }
    
    private String getTimeString(LocalTime time) {
        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("[   HH:mm   ]");
        return formatter.format(time);
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
    
    
    private String getTaskInfoLine(TaskInfo task, int taskNumber, 
            int numberWidth) {
        StringBuilder line = new StringBuilder();
        int taskNameWidth = WIDTH_LINE - WIDTH_ABSOLUTE - WIDTH_TIME - 
                numberWidth;
        line.append(getTaskNumberString(taskNumber, numberWidth));
        line.append(getTimeString(task.endTime));
        line.append(getTaskNameString(task.name, taskNameWidth));
        return line.toString();
    }
    
    private ArrayList<String> formatToArrayList(TaskInfo[] tasks) {
        ArrayList<String> result = new ArrayList<String>();
        sortTask(tasks);
        int numberWidth = numberLength(tasks.length);
        for (int i = 0; i < tasks.length; i++) {
            if (i == 0 || !tasks[i].endDate.equals(tasks[i-1].endDate)) {
                result.add(getDateLine(tasks[i].endDate));
            }
            result.add(getTaskInfoLine(tasks[i], i + 1, numberWidth));
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
    
    public String format(SearchModeInfo searchInfo) {
        TaskInfo[] tasks = searchInfo.getTasks();
        ArrayList<String> formattedTaskArray = formatToArrayList(tasks);
        return arrayListToStringLines(formattedTaskArray);
    }
}
