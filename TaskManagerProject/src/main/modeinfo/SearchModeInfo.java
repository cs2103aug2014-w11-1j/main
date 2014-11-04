package main.modeinfo;

import data.TaskId;
import data.taskinfo.TaskInfo;

/**
 * The ModeInfo for the state when the program is in the search mode.
 */

//@author A0113011L
public class SearchModeInfo implements ModeInfo {

    private String[] suggestions;
    private TaskInfo[] tasks;
    private TaskId[] taskIds;
    private boolean inWaitingMode;
    
    /**
     * Constructor for SearchModeInfo.
     * @param tasks The TaskInfo of the search results.
     * @param taskIds The TaskId of the search results.
     * @param suggestions The suggestions of the search.
     */
    public SearchModeInfo(TaskInfo[] tasks, TaskId[] taskIds, 
            String[] suggestions) {
        this.tasks = tasks;
        this.taskIds = taskIds;
        this.suggestions = suggestions;
    }
    
    /**
     * Get the type of this ModeInfo, which is Type.SEARCH_MODE.
     */
    public Type getType() {
        if (inWaitingMode) {
            return Type.WAITING_MODE;
        } else {
            return Type.SEARCH_MODE;
        }
    }
    
    /**
     * Return the search results as an array of TaskInfo.
     * @return The array of TaskInfo.
     */
    public TaskInfo[] getTasks() {
        return tasks;
    }
    
    /**
     * Return the IDs of the search results as an array of TaskId.
     * @return The array of TaskId.
     */
    public TaskId[] getTaskIds() {
        return taskIds;
    }
    
    public String[] getSuggestions() {
        return suggestions;
    }
    
    public void  makeIntoWaitingModeInfo() {
        inWaitingMode = true;
    }
}
