package manager.result;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class DetailsResult implements Result {

    private TaskInfo task;
    private TaskId taskId;
    
    public DetailsResult(TaskInfo task, TaskId taskId) {
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
