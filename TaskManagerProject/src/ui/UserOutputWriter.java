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
            "mode.com con cols=%1$d lines=%2$d";
    
    public UserOutputWriter(ConsoleReader reader) throws IOException {
        this.reader = reader;
        clearScreen();
        lines = new ArrayList<String>();
    }
    
    public void printOutput(String output) throws IOException {
        String[] array = output.split(System.lineSeparator());
        
        int prevSize = lines.size();
        
        lines.addAll(Arrays.asList(array));
        
        int currentSize = lines.size();
        int height = reader.getTermheight();
        
        int start = prevSize;
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
        
        StringBuilder builder = new StringBuilder();
        for (int i = startLine; i < endLine; i++) {
            builder.append(lines.get(i));
            builder.append(System.lineSeparator());
        }
        
        for (int i = 0; i < numberOfPaddingLines; i++) {
            builder.append(System.lineSeparator());
        }
        reader.printString(builder.toString());
        reader.flushConsole();
        
        currentLine = startLine;
    }
    
    private void clearScreen() throws IOException {
        reader.clearScreen();
        String executedCommand = String.format(EXEC_CLEARSCREEN, 
                reader.getTermwidth(), reader.getTermheight());
        Process p = Runtime.getRuntime().exec(executedCommand);
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