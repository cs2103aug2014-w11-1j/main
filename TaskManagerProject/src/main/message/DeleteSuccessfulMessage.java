package main.message;

import data.TaskId;
import data.taskinfo.TaskInfo;

//@author A0119432L
public class DeleteSuccessfulMessage implements Message {

    private TaskInfo[] tasks;
    
    public DeleteSuccessfulMessage(TaskInfo[] tasks, TaskId[] taskIds) {
        this.tasks = tasks;
    }
    
    public Type getType() {
        return Type.DELETE_SUCCESSFUL;
    }
    
    public TaskInfo[] getTask() {
        return tasks;
    }
}
