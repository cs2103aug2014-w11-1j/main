package manager.result;

import data.TaskId;
import data.taskinfo.TaskInfo;

public interface Result {
    public enum Type {
        ADD_SUCCESS,
        ADD_FAILURE,
        DELETE_SUCCESS,
        DELETE_FAILURE,
        UNDO_SUCCESS,
        UNDO_FAILURE,
        EDIT_MODE_START,
        EDIT_MODE_END,
        EDIT_SUCCESS,
        EDIT_FAILURE,
    }

	public TaskInfo taskInfo = null;
	public TaskId taskId = null;

    public Type getType();    // implement this with the correct type.
}
