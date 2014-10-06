package main.message;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class DetailsMessage implements Message {

    TaskInfo task;
    TaskId taskId;
    
    public DetailsMessage(TaskInfo task, TaskId taskId) {
        this.task = task;
        this.taskId = taskId;
    }
    
    public TaskInfo getTask() {
        return task;
    }
    
    public TaskId getTaskId() {
        return taskId;
    }
    
    @Override
    public Type getType() {
        return Type.DETAILS;
    }

}
