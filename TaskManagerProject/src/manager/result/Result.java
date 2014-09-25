package manager.result;

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

    public Type getType();    // implement this with the correct type.
}
