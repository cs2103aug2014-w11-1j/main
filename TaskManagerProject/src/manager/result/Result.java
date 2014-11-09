package manager.result;

/**
 * The result returned from the manager to the command after an operation.<br>
 * The result is used by the command to update the StateManager on the result
 * of the command execution.
 */
//@author A0065475X
public interface Result {
    public enum Type {
        ADD_SUCCESS,
        ADD_FAILURE,
        DELETE_SUCCESS,
        DELETE_FAILURE,
        UNDO_SUCCESS,
        UNDO_FAILURE,
        REDO_SUCCESS,
        REDO_FAILURE,
        EDIT_MODE_START,
        EDIT_MODE_END,
        SEARCH_MODE_END,
        GO_BACK,
        EDIT_SUCCESS,
        EDIT_FAILURE,
        SEARCH_SUCCESS,
        SEARCH_FAILURE,
        TAG_ADD_SUCCESS,
        TAG_ADD_FAILURE,
        TAG_DELETE_FAILURE,
        TAG_DELETE_SUCCESS,
        DETAILS,
        INVALID_COMMAND,
        INVALID_ARGUMENT,
        FREE_DAY,
        FREE_DAY_FAILURE,
        FREE_TIME,
        REPORT,
        EXIT,
        ALIAS_SUCCESS,
        ALIAS_FAILURE,
        ALIAS_DELETE_SUCCESS,
        ALIAS_DELETE_FAILURE,
        VIEW_ALIAS_SUCCESS
    }

    public Type getType();    // implement this with the correct type.
}
