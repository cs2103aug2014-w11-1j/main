package main.message;

public interface Message {
    public enum Type {
        ENUM_MESSAGE,
        ADD_SUCCESSFUL,
        EDIT_SUCCESSFUL,
        DELETE_SUCCESSFUL,
        FREE_DAY_SEARCH_SUCCESSFUL,
        DETAILS,
        REPORT
    }
    
    public Type getType();
}
