package main.message;

/**
 * Carries information regarding the last command execution.
 */
public interface Message {
    public enum Type {
        ENUM_MESSAGE,
        ADD_SUCCESSFUL,
        EDIT_SUCCESSFUL,
        DELETE_SUCCESSFUL,
        FREE_DAY_SEARCH_SUCCESSFUL,
        FREE_TIME_SEARCH_SUCCESSFUL,
        EDIT_ALIAS,
        VIEW_ALIAS,
        DETAILS,
        REPORT
    }
    
    public Type getType();
}
