package ui;

import java.util.Scanner;

public class UserInputReader {
    private Scanner scanner;
    
    public UserInputReader() {
        scanner = new Scanner(System.in);
    }
    public String readInput() {
        return scanner.nextLine();
    }
}
