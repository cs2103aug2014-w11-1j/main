package main.modeinfo;

import data.TaskId;

public class EditModeInfo implements ModeInfo {

    TaskId taskId;
    public EditModeInfo(TaskId taskId) {
        this.taskId = taskId;
    }
    
    public Type getType() {
        return Type.EDIT_MODE;
    }
    
    public TaskId getTaskId() {
        return taskId;
    }

}
