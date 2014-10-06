package manager.result;

import data.taskinfo.TaskInfo;

public class DetailsResult implements Result {

    public TaskInfo task;
    
    public DetailsResult(TaskInfo task) {
        this.task = task;
    }
    
    public TaskInfo getTask() {
        return task;
    }
    
    @Override
    public Type getType() {
        return Type.DETAILS;
    }

}
