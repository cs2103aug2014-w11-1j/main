package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import jline.ConsoleReader;

public class UserOutputWriter {
    ArrayList<String> lines;
    ConsoleReader reader;
    int currentLine;
    
    private final static String EXEC_CLEARSCREEN = 
            "mode.com con cols=80 lines=25";
    
    public UserOutputWriter(ConsoleReader reader) throws IOException {
        this.reader = reader;
        clearScreen();
        lines = new ArrayList<String>();
    }
    
    public void printOutput(String output) throws IOException {
        String[] array = output.split(System.lineSeparator());
        
        lines.addAll(Arrays.asList(array));
        
        int currentSize = lines.size();
        int height = reader.getTermheight();
        
        int start = Math.max(0,  currentSize - height + 1);
        show(start);
    }
    
    public void show(int startLine) throws IOException {
        clearScreen();
        int numberOfLines = reader.getTermheight();
        int numberOfOutputLines = numberOfLines - 1;
        int numberOfStoredLines = lines.size();
        
        int endLine = Math.min(numberOfStoredLines, 
                startLine + numberOfOutputLines);
        
        int numberOfAvailableLines = endLine - startLine;
        
        int numberOfPaddingLines = numberOfOutputLines - numberOfAvailableLines;
        
        for (int i = startLine; i < endLine; i++) {
            reader.printString(lines.get(i));
            reader.printNewline();
        }
        
        for (int i = 0; i < numberOfPaddingLines; i++) {
            reader.printNewline();
        }
        reader.flushConsole();
        
        currentLine = startLine;
    }
    
    private void clearScreen() throws IOException {
        reader.clearScreen();
        Process p = Runtime.getRuntime().exec("mode.com con cols=80 lines=25");
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            
        }
        reader.flushConsole();
    }
    
    public void scrollUp() throws IOException {
        if (currentLine > 0) {
            show(currentLine - 1);
        }
    }
    
    public void scrollDown() throws IOException {
        if (currentLine + reader.getTermheight() < lines.size() + 1) {
            show(currentLine + 1);
        }
    }
}