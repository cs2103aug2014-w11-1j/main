package main.formatting;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import main.response.SearchResponse;
import data.taskinfo.Priority;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

public class SearchFormatter {
    private final static String LINE_TASK_NAME = 
            "Task: %1$s";
    private final static String LINE_PRIORITY = 
            "Priority: %1$s";
    private final static String LINE_DATE_TIME = 
            "Date/Time: %1$s %2$s";
    private final static String STRING_DATE = "%1$d/%1$d/%1$d";
    private final static String STRING_TIME = "%1$d:%1$d %3$s";
    
    public String format(SearchResponse response) {
        TaskInfo[] searchResults = response.getSearchResults();
        ArrayList<String> formattedLines = new ArrayList<String>();
        int spaces = numberLength(searchResults.length);
        
        for (int i = 0; i < searchResults.length; i++) {
            ArrayList<String> formattedTask = formatTask(searchResults[i]);
            ArrayList<String> taskWithLines = 
                    addTaskNumber(formattedTask, i + 1, spaces);
            formattedLines.addAll(taskWithLines);
        }
        return combineLines(formattedLines);
    }
    
    private String getTagLine(TaskInfo task) {
        StringBuilder tagLine = new StringBuilder("Tagged : ");
        Tag[] tags = task.tags;
        
        for (int i = 0; i < tags.length; i++) {
            if (i > 0)
                tagLine.append(',');
            tagLine.append(tags[i].toString());
        }
        return tagLine.toString();
    }
    
    private String getTimeString(LocalTime time) {
        int hour = time.getHour();
        int minute = time.getMinute();
        String type;
        
        if (hour == 0) {
            type = "AM";
            hour = 12;
        } else if (hour > 0 && hour < 12) {
            type = "AM";
        } else if (hour == 12) {
            type = "PM";
        } else {
            type = "PM";
            hour = hour - 12;
        }
        
        return String.format(STRING_TIME, hour, minute, type);
    }
    
    private String getDateString(LocalDate date) {
        int dayOfMonth = date.getDayOfMonth();
        int monthNumber = date.getMonth().getValue();
        int year = date.getYear();
        
        return String.format(STRING_DATE, dayOfMonth, monthNumber, year);
    }
    
    private String getDateTimeLine(TaskInfo task) {
        String timeString = getTimeString(task.endTime);
        String dateString = getDateString(task.endDate);
        
        return String.format(LINE_DATE_TIME, timeString, dateString);
    }
    
    private String getPriorityString(Priority priority) {
        String priorityString = "";
        
        switch(priority) {
            case HIGH:
                priorityString = "High";
            case MEDIUM:
                priorityString = "Medium";
            case LOW:
                priorityString = "Low";
        }
        
        return priorityString;
    }
    
    private ArrayList<String> formatTask(TaskInfo task) {
        ArrayList<String> lines = new ArrayList<String>();
        
        lines.add(String.format(LINE_TASK_NAME, task.name));
        lines.add(getDateTimeLine(task));
        lines.add(getTagLine(task));
        lines.add(String.format(LINE_PRIORITY, 
                getPriorityString(task.priority)));
        
        return lines;
    }
    
    private String getIndent(int spaces) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < spaces; i++) {
            indent.append(" ");
        }
        return indent.toString();
    }
    
    private ArrayList<String> addTaskNumber(ArrayList<String> formattedTask, 
            int taskNumber, int spaces) {
        
        ArrayList<String> result = new ArrayList<String>();
        String firstLineIndent = getIndent(spaces - numberLength(taskNumber) - 1);
        String lineNumberString = Integer.toString(taskNumber);
        
        String processedFirstLine = 
               firstLineIndent + lineNumberString + ")" + formattedTask.get(0);
        result.add(processedFirstLine);
        
        for (int i = 1; i < formattedTask.size(); i++) {
            String lineIndent = getIndent(spaces);
            String processedLine = lineIndent + formattedTask.get(i);
            result.add(processedLine);
        }
        return result;
    }
    
    private String combineLines(ArrayList<String> lines) {
        StringBuilder combined = new StringBuilder();
        for (String line : lines) {
            combined.append(line);
            combined.append(System.lineSeparator());
        }
        return combined.toString();
    }
    
    private int numberLength(int number) {
        return Integer.toString(number).length();
    }
}
