package manager.result;

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
        INVALID_ARGUMENT
    }

    public Type getType();    // implement this with the correct type.
}
