package main.message;

import data.taskinfo.TaskInfo;

public class EditSuccessfulMessage implements Message{
    
    TaskInfo task;
    
    public EditSuccessfulMessage(TaskInfo task) {
        this.task = task;
    }
    
    public Type getType() {
        return Type.EDIT_SUCCESSFUL;
    }
}
