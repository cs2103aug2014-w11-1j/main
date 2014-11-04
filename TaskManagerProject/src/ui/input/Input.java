package ui.input;

//@author A0113011L
public interface Input {
    public enum Type {
        INPUT_STRING,
        INPUT_OPERATION
    }
    
    public Type getType();
}
