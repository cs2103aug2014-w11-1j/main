package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jline.ConsoleReader;

public class UserOutputWriter {
    List<String> lines;
    List<Integer> headers;
    ConsoleReader reader;
    int currentLine;
    
    private final static String EXEC_CLEARSCREEN = 
            "mode.com con cols=%1$d lines=%2$d";
    
    private final static String MESSAGE_WELCOME = "Welcome to Taskline.";
    
    public UserOutputWriter(ConsoleReader reader) throws IOException {
        this.reader = reader;
        clearScreen();
        lines = new ArrayList<String>();
        headers = new ArrayList<Integer>();
        
        lines.add(MESSAGE_WELCOME);
        headers.add(0);
        show(0);
    }
    
    public void addHeader(String header) {
        if (header != null) {
            headers.add(lines.size());
            lines.add(header);
        }
    }
    
    public void printOutput(String header, String output) throws IOException {
        int prevSize = lines.size();
        
        addHeader(header);
        String[] array = output.split(System.lineSeparator());
        
        lines.addAll(Arrays.asList(array));
        
        int start = prevSize;
        show(start);
    }
    
    private int getHeader(int lineNumber) {
        int headerLineNumber = -1;
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i) <= lineNumber) {
                headerLineNumber = headers.get(i);
            }
        }
        return headerLineNumber;
    }
    
    private void show(int startLine) throws IOException {
        clearScreen();
        int numberOfLines = reader.getTermheight();
        int numberOfOutputLines = numberOfLines - 1;
        int numberOfStoredLines = lines.size();
        
        int endLine = Math.min(numberOfStoredLines, 
                startLine + numberOfOutputLines);
        
        int numberOfAvailableLines = endLine - startLine;
        
        int numberOfPaddingLines = numberOfOutputLines - numberOfAvailableLines;
        
        StringBuilder builder = new StringBuilder();
        builder.append(lines.get(getHeader(startLine)));
        builder.append(System.lineSeparator());
        for (int i = startLine + 1; i < endLine; i++) {
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
        if (currentLine < lines.size() - 2) {
            show(currentLine + 1);
        }
    }
    
    private int findPreviousHeader(int currentHeader) {
        int previousHeader = -1;
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i) < currentHeader) {
                previousHeader = headers.get(i);
            }
        }
        return previousHeader;
    }
    
    private int findNextHeader(int currentHeader) {
        for (int i = 0; i < headers.size(); i++) {
            if (headers.get(i) > currentHeader) {
                return headers.get(i);
            }
        }
        return -1;
    }
    
    public void prevCommand() throws IOException {
        int currentHeader = getHeader(currentLine);
        int nextLine;
        if (currentHeader == currentLine) {
            nextLine = findPreviousHeader(currentHeader);
            if (nextLine == -1) {
                nextLine = 0;
            }
        } else {
            nextLine = currentHeader;
        }
        show(nextLine);
    }
    
    public void nextCommand() throws IOException {
        int currentHeader = getHeader(currentLine);
        int nextLine = findNextHeader(currentHeader);
        if (nextLine == -1) {
            nextLine = lines.size() - 2;
        }
        show(nextLine);
    }
}