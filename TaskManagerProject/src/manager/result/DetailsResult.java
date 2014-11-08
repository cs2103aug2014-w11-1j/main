package manager.result;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class DetailsResult implements Result {

    private TaskInfo[] tasks;
    private TaskId[] taskIds;
    
    public DetailsResult(TaskInfo[] tasks, TaskId[] taskIds) {
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
