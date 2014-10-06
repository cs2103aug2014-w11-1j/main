package main.modeinfo;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class EditModeInfo implements ModeInfo {

    private TaskId taskId;
    private TaskInfo taskInfo;
    
    public EditModeInfo(TaskInfo taskInfo, TaskId taskId) {
        this.taskId = taskId;
        this.taskInfo = taskInfo;
    }
    
    public Type getType() {
        return Type.EDIT_MODE;
    }
    
    public TaskId getTaskId() {
        return taskId;
    }

}
