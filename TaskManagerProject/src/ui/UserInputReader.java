package ui;

import java.io.IOException;

import jline.ConsoleReader;

public class UserInputReader {
    private ConsoleReader reader;
    
    public UserInputReader() throws IOException {
        reader = new ConsoleReader();
    }
    
    public String readInput() throws IOException {
        return reader.readLine(">");
    }
}
