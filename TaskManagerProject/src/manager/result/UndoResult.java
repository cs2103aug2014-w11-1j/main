package manager.result;

import data.TaskId;

//@author A0065475X
public class UndoResult implements Result {
    private final TaskId[] taskIds;
    private final Type type;
    private final int times;
    
    public UndoResult(Type type, TaskId[] taskIds, int times) {
        assert type == Type.UNDO_SUCCESS || type == Type.REDO_SUCCESS;
        this.taskIds = taskIds;
        this.type = type;
        this.times = times;
    }
    
    @Override
    public Type getType() {
        return type;
    }
    
    public TaskId[] getTaskIds() {
        return taskIds;
    }
    
    public int getNumberOfTimes() {
        return times;
    }

}
