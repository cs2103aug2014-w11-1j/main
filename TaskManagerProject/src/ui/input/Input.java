package ui.input;

public interface Input {
    public enum Type {
        INPUT_STRING,
        INPUT_OPERATION
    }
    
    public Type getType();
}
