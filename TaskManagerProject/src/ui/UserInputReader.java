package ui;

import java.io.IOException;

import jline.ConsoleReader;
import ui.input.Input;
import ui.input.InputOperation;
import ui.input.InputString;

public class UserInputReader {
    private ConsoleReader reader;
    enum Mode {
        SCROLL_MODE,
        INPUT_MODE
    }

    private final static int KEY_ESC = 27;
    private final static int KEY_ENTER = 13;
    private final static int KEY_UP = 16;
    private final static int KEY_DOWN = 14;
    private final static int KEY_LEFT = 2;
    private final static int KEY_RIGHT = 6;
    private final static int KEY_BACKSPACE = 8;
    
    Mode currentMode;
    
    public UserInputReader(ConsoleReader reader) throws IOException {
        this.reader = reader;
        currentMode = Mode.INPUT_MODE;
    }
    
    public Input getInput() throws IOException {
        Input result = null;
        
        while (result == null) {
            int key = readKey();
            result = processKey(key);
        }
        
        return result;
    }
    
    private Input processKey(int key) throws IOException {
        switch(currentMode) {
            case SCROLL_MODE :
                return processKeyScroll(key);
            case INPUT_MODE :
                return processKeyInput(key);
        }
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Input processKeyScroll(int key) throws IOException {
        switch(key) {
            case KEY_UP :
                return new InputOperation(InputOperation.Operation.SCROLL_UP);
            case KEY_DOWN :
                return new InputOperation(InputOperation.Operation.SCROLL_DOWN);
            case KEY_ESC :
                switchMode(Mode.INPUT_MODE);
                return null;
            default :
                return null;
        }
    }

    private Input processKeyInput(int key) throws IOException {
        switch(key) {
            case KEY_ESC :
                switchMode(Mode.SCROLL_MODE);
                return null;
            case KEY_ENTER :
                reader.printNewline();
                String bufferString = reader.getCursorBuffer().toString();
                InputString input = 
                        new InputString(bufferString);
                reader.getHistory().addToHistory(bufferString);
                reader.getCursorBuffer().clearBuffer();
                return input;
            case KEY_UP :
                if (reader.getHistory().previous()) {
                    clearLine();
                    reader.putString(reader.getHistory().current());
                    reader.flushConsole();
                }
                return null;
            case KEY_DOWN :
                if (reader.getHistory().next()) {
                    clearLine();
                    reader.putString(reader.getHistory().current());
                    reader.flushConsole();
                }
                return null;
            case KEY_BACKSPACE :
                reader.backspace();
                return null;
            default :
                reader.putString(Character.toString((char)key));
                reader.flushConsole();
                return null;
        }
    }

    private int readKey() throws IOException {
        return reader.readVirtualKey();
    }
    
    private void switchMode(Mode newMode) throws IOException {
        clearLine();
        currentMode = newMode;
    }
    
    private void clearLine() throws IOException {
        while (reader.getCursorBuffer().length() > 0) {
            reader.backspace();
        }
    }
}
