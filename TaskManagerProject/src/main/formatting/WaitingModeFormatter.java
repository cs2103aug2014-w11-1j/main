package main.formatting;

import java.util.ArrayList;
import java.util.List;

import main.modeinfo.SearchModeInfo;
import data.TaskId;
import data.taskinfo.TaskInfo;

//@author A0113011L
/**
 * Formatter for WaitingMode.
 * Example:
 * Did you mean:
 * 1) Complete taskA
 * 2) complete taskB
 * 
 * @author Nathan
 *
 */
public class WaitingModeFormatter {
    private final static String PROMPT = "Did you mean:";
    private final static String FORMAT_CHOICE = "%1$d) %2$s";
    private final static String EMPTY = "No tasks found.";
    
    private ArrayList<String> formatToArrayList(TaskInfo[] tasks, TaskId[] taskId) {
        assert tasks.length > 0;
        ArrayList<String> result = new ArrayList<String>();
        result.add(PROMPT);
        for (int i = 0; i < tasks.length; i++) {
            int lineNumber = i + 1;
            result.add(String.format(FORMAT_CHOICE, lineNumber, tasks[i].name));
        }
        return result;
    }
    
    private ArrayList<String> formatEmpty(TaskInfo[] tasks, TaskId[] taskId) {
        assert tasks.length == 0;
        ArrayList<String> result = new ArrayList<String>();
        result.add(EMPTY);
        return result;
    }
    
    private String listToStringLines(List<String> lines) {
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            result.append(line);
            result.append(System.lineSeparator());
        }
        return result.toString();
    }
    
    /**
     * Format the SearchModeInfo into a String.
     * Note that the ModeInfo for Waiting reuses the one that is used in the
     * Search.
     * @param searchInfo The SearchModeInfo to be formatted.
     * @return The formatting result.
     */
    public String format(SearchModeInfo searchInfo) {
        TaskInfo[] tasks = searchInfo.getTasks();
        TaskId[] taskIds = searchInfo.getTaskIds();
        List<String> formattedTaskArray;
        switch(tasks.length){
            case 0 :
                formattedTaskArray = formatEmpty(tasks, taskIds);
                break;
            default :
                formattedTaskArray = formatToArrayList(tasks, taskIds);
                break;
        }
        formattedTaskArray.add("");
        return listToStringLines(formattedTaskArray);
    }
}
