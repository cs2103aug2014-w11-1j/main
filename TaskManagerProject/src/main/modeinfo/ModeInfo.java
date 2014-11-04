package main.modeinfo;

/**
 * An Interface to pass the current state of the program to the Formatter.
 *
 */

//@author A0113011L
public interface ModeInfo {
    public enum Type {
        EMPTY_MODE,
        SEARCH_MODE,
        WAITING_MODE,
        EDIT_MODE
    }
    
    /**
     * Get the Type of the ModeInfo.
     * @return
     */
    public Type getType();
}
