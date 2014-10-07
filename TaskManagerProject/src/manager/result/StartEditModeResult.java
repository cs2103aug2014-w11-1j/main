package manager.result;

import manager.result.Result.Type;
import data.TaskId;
import data.taskinfo.TaskInfo;

public class StartEditModeResult implements Result {

    private final TaskId taskId;

    public StartEditModeResult(TaskId taskId){
        this.taskId = taskId;
    }
    
    @Override
    public Type getType() {
        return Type.EDIT_MODE_START;
    }

    public TaskId getTaskId(){
        return taskId;
    }
}
