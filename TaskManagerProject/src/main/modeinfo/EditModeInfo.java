package main.modeinfo;

import data.taskinfo.TaskInfo;

public class EditModeInfo implements ModeInfo {

    TaskInfo task;
    public EditModeInfo(TaskInfo task) {
        this.task = task;
    }
    
    public Type getType() {
        return Type.EDIT_MODE;
    }
    
    public TaskInfo getTask() {
        return task;
    }

}
