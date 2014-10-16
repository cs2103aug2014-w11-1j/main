package manager.result;

import data.TaskId;

public class UndoResult implements Result {
    private final TaskId[] taskIds;
    private final Type type;
    
    public UndoResult(Type type, TaskId[] taskIds) {
        assert type == Type.UNDO_SUCCESS || type == Type.REDO_SUCCESS;
        this.taskIds = taskIds;
        this.type = type;
    }
    
    @Override
    public Type getType() {
        return type;
    }
    
    public TaskId[] getTaskIds() {
        return taskIds;
    }

}
