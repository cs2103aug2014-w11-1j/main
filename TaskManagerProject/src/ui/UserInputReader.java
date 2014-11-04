package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jline.ArgumentCompletor;
import jline.ConsoleReader;
import ui.input.Input;
import ui.input.InputOperation;
import ui.input.InputString;

//@author A0113011L
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
    private final static int KEY_DELETE = 127;
    private final static int KEY_PGUP = 11;
    private final static int KEY_PGDN = 12;
    private final static int KEY_TAB = 9;
    
    private final static int ASCII_SPACE = 32;
    
    private final static String PROMPT_INPUT = ">";
    private final static String PROMPT_SCROLL = "--- SCROLL MODE ---";
    
    Mode currentMode;
    
    ArgumentCompletor completor;
    
    public UserInputReader(ConsoleReader reader, 
            ArgumentCompletor completor) throws IOException {
        this.reader = reader;
        this.completor = completor;
        currentMode = Mode.INPUT_MODE;
    }
    
    public Input getInput() throws IOException {
        switchMode(currentMode);
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
            case KEY_PGDN :
                return new InputOperation(InputOperation.Operation.NEXT_COMMAND);
            case KEY_PGUP :
                return new InputOperation(InputOperation.Operation.PREV_COMMAND);
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
                reader.getHistory().moveToEnd();
                reader.getCursorBuffer().clearBuffer();
                return input;
            case KEY_UP :
                if (reader.getHistory().previous()) {
                    clearInput();
                    reader.putString(reader.getHistory().current());
                    reader.flushConsole();
                }
                return null;
            case KEY_DOWN :
                if (reader.getHistory().next()) {
                    clearInput();
                    reader.putString(reader.getHistory().current());
                    reader.flushConsole();
                }
                return null;
            case KEY_BACKSPACE :
                reader.backspace();
                reader.flushConsole();
                return null;
            case KEY_LEFT :
                int currentCursorPosition = reader.getCursorBuffer().cursor;
                if (reader.setCursorPosition(currentCursorPosition - 1)) {
                    reader.flushConsole();
                }
                return null;
            case KEY_RIGHT :
                int currentPosition = reader.getCursorBuffer().cursor;
                if (reader.setCursorPosition(currentPosition + 1)) {
                    reader.flushConsole();
                }
                return null;
            case KEY_DELETE :
                reader.delete();
                reader.flushConsole();
                return null;
            case KEY_TAB :
                String currentString = reader.getCursorBuffer().toString();
                
                List<String> possibleList = new ArrayList<String>();
                if (completor.complete(currentString, reader.getCursorBuffer().cursor, possibleList) >= 0) {
                    if (possibleList.size() == 1) {
                        clearInput();
                        reader.putString(possibleList.get(0));
                        reader.flushConsole();
                    }
                }
                return null;
            default :
                if (key >= ASCII_SPACE) {
                    reader.putString(Character.toString((char)key));
                    reader.flushConsole();
                }
                return null;
        }
    }

    private int readKey() throws IOException {
        return reader.readVirtualKey();
    }
    
    private void switchMode(Mode newMode) throws IOException {
        clearLine();
        currentMode = newMode;
        
        switch (newMode) {
            case INPUT_MODE :
                reader.printString(PROMPT_INPUT);
                break;
            case SCROLL_MODE :
                reader.printString(PROMPT_SCROLL);
                break;
        }
        reader.flushConsole();
    }
    
    private void clearInput() throws IOException {
        while (reader.getCursorBuffer().length() > 0) {
            reader.backspace();
        }
    }
    
    private void clearPrompt() throws IOException {
        for (int i = 0; i < reader.getTermwidth(); i++) {
            reader.printString("\b");
        }
        for (int i = 0; i < reader.getTermwidth() - 1; i++) {
            reader.printString(" ");;
        }
        for (int i = 0; i < reader.getTermwidth(); i++) {
            reader.printString("\b");
        }
    }
    
    private void clearLine() throws IOException {
        clearInput();
        clearPrompt();
    }
}
