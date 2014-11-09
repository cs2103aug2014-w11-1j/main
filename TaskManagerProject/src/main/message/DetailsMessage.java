package main.message;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class DetailsMessage implements Message {

    TaskInfo[] tasks;
    TaskId[] taskIds;
    
    public DetailsMessage(TaskInfo[] tasks, TaskId[] taskIds) {
        this.tasks = tasks;
        this.taskIds = taskIds;
    }
    
    public TaskInfo[] getTasks() {
        return tasks;
    }
    
    public TaskId[] getTaskIds() {
        return taskIds;
    }
    
    @Override
    public Type getType() {
        return Type.DETAILS;
    }

}
