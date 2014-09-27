package main.message;

import data.taskinfo.TaskInfo;

public class DeleteSuccessfulMessage implements Message {

    private TaskInfo task;
    
    public DeleteSuccessfulMessage(TaskInfo task) {
        this.task = task;
    }
    
    public Type getType() {
        return Type.DELETE_SUCCESSFUL;
    }
    
    public TaskInfo getTask() {
        return task;
    }
}
