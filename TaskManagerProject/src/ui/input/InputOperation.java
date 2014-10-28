package ui.input;

import ui.input.Input.Type;

public class InputOperation implements Input {
    public enum Operation {
        SCROLL_UP,
        SCROLL_DOWN,
        PREV_COMMAND,
        NEXT_COMMAND
    }
    
    Operation operation;
    public InputOperation(Operation operation) {
        this.operation = operation;
    }
    
    public Type getType() {
        return Type.INPUT_OPERATION;
    }
    
    public Operation getOperation() {
        return operation;
    }
}
