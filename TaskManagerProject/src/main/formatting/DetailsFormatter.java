package main.formatting;

import java.util.ArrayList;

import main.formatting.utility.DetailsUtility;
import main.message.DetailsMessage;
import data.TaskId;
import data.taskinfo.TaskInfo;

//@author A0113011L
/**
 * <pre>
 * Formatter for the DetailsMessage.
 * Format example:
 * Task [a9f]
 *    Name: eat apples for dinner
 *    Time: 17:30 (PM)
 *    Date: Tuesday, 17 Feb 2015
 *    Tags: taskline, food
 *    Priority: High
 *    Description: I will die if I don't eat my apples!
 * </pre>
 */
public class DetailsFormatter {
    DetailsUtility detailsUtility;

    public DetailsFormatter() {
        detailsUtility = new DetailsUtility();
    }
    
    private String arrayListToString(ArrayList<String> lines) {
        StringBuilder builder = new StringBuilder("");
        for (String line : lines) {
            builder.append(line);
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }
    
    /**
     * Format the DetailsMessage to a String.
     * @param message The DetailsMessage to be formatted.
     * @return The formatted Message.
     */
    public String format(DetailsMessage message) {
        TaskInfo[] tasks = message.getTasks();
        TaskId[] taskIds = message.getTaskIds();
        assert tasks.length == taskIds.length;

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < tasks.length; i++) {
            builder.append(formatDetails(tasks[i], taskIds[i]));
        }
        
        return builder.toString();
    }

    private String formatDetails(TaskInfo task, TaskId taskId) {
        return arrayListToString(
                detailsUtility.formatToArray(task, taskId)) + System.lineSeparator();
    }
}
