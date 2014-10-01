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
        SEARCH_SUCCESS,
        TAG_ADD_FAILURE,
        TAG_ADD_SUCCESS,
        TAG_DELETE_FAILURE,
        TAG_DELETE_SUCCESS
        
    }

    public Type getType();    // implement this with the correct type.
}
