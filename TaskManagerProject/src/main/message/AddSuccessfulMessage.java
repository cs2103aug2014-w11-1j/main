package main.message;

import data.taskinfo.TaskInfo;

public class AddSuccessfulMessage implements Message {

    private TaskInfo task;
    
    public AddSuccessfulMessage(TaskInfo task) {
        this.task = task;
    }
    
    public Type getType() {
        return Type.ADD_SUCCESSFUL;
    }
    
    public TaskInfo getTask() {
        return task;
    }

}
