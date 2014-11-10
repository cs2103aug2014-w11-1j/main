package ui.input;


//@author A0113011L
/**
 * Represents a String input from the user.
 */
public class InputString implements Input {

    private String inputString;
    
    public InputString(String string) {
        this.inputString = string;
    }
    
    @Override
    public Type getType() {
        return Type.INPUT_STRING;
    }
    
    public String getString() {
        return inputString;
    }

}
