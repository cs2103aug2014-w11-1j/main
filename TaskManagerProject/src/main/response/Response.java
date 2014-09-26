package main.response;

public interface Response {

    public enum Type {
        EMPTY_STRING,   // print no output
        ENUM_MESSAGE,    // print the message type stored in the enum.
        SEARCH_RESULTS,
        ADD_SUCCESSFUL,
        DELETE_SUCCESSFUL,
        EDIT_SUCCESS,
        EDIT_MODE,
    }
    
    public Type getType();
}
