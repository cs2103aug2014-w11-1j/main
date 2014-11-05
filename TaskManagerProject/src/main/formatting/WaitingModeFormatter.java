package main.formatting;

import java.util.ArrayList;

import main.modeinfo.SearchModeInfo;
import data.TaskId;
import data.taskinfo.TaskInfo;

//@author A0113011L
public class WaitingModeFormatter {
    private final static String PROMPT = "Did you mean:";
    private final static String FORMAT_CHOICE = "%1$d)%2$s";
    
    private ArrayList<String> formatToArrayList(TaskInfo[] tasks, TaskId[] taskId) {
        ArrayList<String> result = new ArrayList<String>();
        result.add(PROMPT);
        for (int i = 0; i < tasks.length; i++) {
            int lineNumber = i + 1;
            result.add(String.format(FORMAT_CHOICE, lineNumber, tasks[i].name));
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
        formattedTaskArray.add("");
        return arrayListToStringLines(formattedTaskArray);
    }
}
