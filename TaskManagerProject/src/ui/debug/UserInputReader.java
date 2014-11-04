package ui.debug;

import java.util.Scanner;

//@author A0113011L
public class UserInputReader {
    private Scanner scanner;
    
    public UserInputReader() {
        scanner = new Scanner(System.in);
    }
    public String readInput() {
        return scanner.nextLine();
    }
}
