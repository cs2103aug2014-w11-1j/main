package main.message;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class AddSuccessfulMessage implements Message {

    private TaskInfo task;
    
    public AddSuccessfulMessage(TaskInfo task, TaskId taskId) {
        this.task = task;
    }
    
    public Type getType() {
        return Type.ADD_SUCCESSFUL;
    }
    
    public TaskInfo getTask() {
        return task;
    }

}
