package ui;

import java.io.IOException;

import main.MainController;

public class UIDisplay {
    private UserInputReader userInputReader;
    private UserOutputWriter userOutputWriter;
    
    private final MainController mainController;
    
    private final static String MESSAGE_WELCOME = "Welcome to Taskline." + 
            System.lineSeparator();
    
    public UIDisplay(MainController mainController) {
        this.mainController = mainController;
        
        try {
            userInputReader = new UserInputReader();
            userOutputWriter = new UserOutputWriter();
        } catch(IOException e) {
            System.out.println("IOException : " + e.getMessage());
        }
    }
    
    /**
     * Called from main
     */
    public void commandLoopIteration() {
        userOutputWriter.printOutput(MESSAGE_WELCOME);
        while (!isReadyToExit()) {
            try {
            String input = userInputReader.readInput();
            String output = mainController.runCommand(input);
            userOutputWriter.printOutput(output);
            } catch(IOException e) {
                System.out.println("Something went wrong.");
                System.out.println("Please try again.");
            }
        }
    }
    
    public boolean isReadyToExit() {
        return mainController.isReadyToExit();
    }
}
