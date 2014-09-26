package main.modeinfo;

import data.taskinfo.TaskInfo;

public class SearchModeInfo implements ModeInfo {

    private TaskInfo[] tasks;
    
    public SearchModeInfo(TaskInfo[] tasks) {
        this.tasks = tasks;
    }
    
    public Type getType() {
        return Type.SEARCH_MODE;
    }
    
    public TaskInfo[] getTasks() {
        return tasks;
    }
}
