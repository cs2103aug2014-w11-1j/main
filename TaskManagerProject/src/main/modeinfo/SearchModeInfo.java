package main.modeinfo;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class SearchModeInfo implements ModeInfo {

    private TaskInfo[] tasks;
    private TaskId[] taskIds;
    
    public SearchModeInfo(TaskInfo[] tasks, TaskId[] taskIds) {
        this.tasks = tasks;
        this.taskIds = taskIds;
    }
    
    public Type getType() {
        return Type.SEARCH_MODE;
    }
    
    public TaskInfo[] getTasks() {
        return tasks;
    }
    
    public TaskId[] getTaskIds() {
        return taskIds;
    }
}
