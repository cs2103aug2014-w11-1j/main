package main.message;

public interface Message {
    public enum Type {
        ENUM_MESSAGE,
        ADD_SUCCESSFUL,
        EDIT_SUCCESSFUL
    }
    
    public Type getType();
}
