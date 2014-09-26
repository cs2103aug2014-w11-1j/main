package main.modeinfo;

public interface ModeInfo {
    public enum Type {
        EMPTY_MODE,
        SEARCH_MODE,
        EDIT_MODE
    }
    
    public Type getType();
}
