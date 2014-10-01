package main.formatting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import main.modeinfo.SearchModeInfo;
import data.TaskId;
import data.taskinfo.TaskInfo;

public class SearchModeFormatter {
    private final static int WIDTH_LINE = 80;
    private final static int WIDTH_TIME = 14;
    private final static int WIDTH_ABSOLUTE = 7;
    
    class InfoId {
        public TaskInfo taskInfo;
        public TaskId taskId;
        public InfoId(TaskInfo taskInfo, TaskId taskId) {
            this.taskInfo = taskInfo;
            this.taskId = taskId;
        }
    }
    public void sortTask(TaskInfo[] tasks, TaskId[] taskIds) {
        InfoId[] combinedList = new InfoId[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            combinedList[i] = new InfoId(tasks[i], taskIds[i]);
        }
        Arrays.sort(combinedList, new Comparator<InfoId>() {
            public int compare(InfoId task1, InfoId task2) {
                if (task1.taskInfo.endDate.compareTo(task2.taskInfo.endDate) < 0) {
                    return -1;
                } else if (task1.taskInfo.endDate.compareTo(task2.taskInfo.endDate) > 0) {
                    return 1;
                } else {
                    return task1.taskInfo.endTime.compareTo(task2.taskInfo.endTime);
                }
            }
        });
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = combinedList[i].taskInfo;
            taskIds[i] = combinedList[i].taskId;
        }
    }
    
    private String getDateLine(LocalDate date) {
        DateTimeFormatter formatter = 
                DateTimeFormatter.ofPattern("E, d MMM u");
        String dateString = formatter.format(date);
        return dateString + " ---";
    }
    
    private String getTimeString(LocalTime time) {
        DateTimeFormatter formatter = 
                DateTimeFormatter.ofPattern("HH:mm");
        String addedFormat = "[   %1$s   ] ";
        return String.format(addedFormat, formatter.format(time));
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
        return String.format("- [%1$s]", taskId.toString());
    }
    
    private String getTaskInfoLine(TaskInfo task, TaskId taskId, 
            int taskNumber, 
            int numberWidth) {
        StringBuilder line = new StringBuilder();
        int taskNameWidth = WIDTH_LINE - WIDTH_ABSOLUTE - WIDTH_TIME - 
                numberWidth;
        line.append(getTaskNumberString(taskNumber, numberWidth));
        line.append(getTimeString(task.endTime));
        line.append(getTaskNameString(task.name, taskNameWidth));
        line.append(getAbsoluteTaskIdString(taskId));
        return line.toString();
    }
    
    private ArrayList<String> formatToArrayList(TaskInfo[] tasks, 
            TaskId[] taskIds) {
        ArrayList<String> result = new ArrayList<String>();
        sortTask(tasks, taskIds);
        int numberWidth = numberLength(tasks.length) + 2;
        for (int i = 0; i < tasks.length; i++) {
            if (i == 0 || !tasks[i].endDate.equals(tasks[i-1].endDate)) {
                result.add(getDateLine(tasks[i].endDate));
            }
            result.add(getTaskInfoLine(tasks[i], taskIds[i], 
                    i + 1, numberWidth));
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
        TaskId[] taskIds = searchInfo.getTaskIds();
        ArrayList<String> formattedTaskArray = 
                formatToArrayList(tasks, taskIds);
        return arrayListToStringLines(formattedTaskArray);
    }
}
