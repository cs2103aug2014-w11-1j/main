package ui;

import java.io.IOException;

import jline.ConsoleReader;
import main.MainController;
import ui.input.Input;
import ui.input.InputString;

public class UIDisplay {
    private UserInputReader userInputReader;
    private UserOutputWriter userOutputWriter;
    
    private final MainController mainController;
    
    private final static String MESSAGE_WELCOME = "Welcome to Taskline." + 
            System.lineSeparator();
    
    public UIDisplay(MainController mainController) {
        this.mainController = mainController;
        try {
            ConsoleReader reader = new ConsoleReader();
            reader.clearScreen();
            userInputReader = new UserInputReader(reader);
            userOutputWriter = new UserOutputWriter(reader);
        } catch(IOException e) {
            System.out.println("IOException : " + e.getMessage());
        }
    }
    
    /**
     * Called from main
     */
    public void commandLoopIteration() {
        try {
            userOutputWriter.printOutput(MESSAGE_WELCOME);
        } catch(IOException e) {
            System.out.println(MESSAGE_WELCOME);
        }
        while (!isReadyToExit()) {
            try {
                Input input = userInputReader.getInput();
                processInput(input);
            }
            catch(IOException e) {
                System.out.println("Something went wrong.");
                System.out.println("Please try again.");
            }
        }
    }
    
    public void processInput(Input input) throws IOException {
        switch (input.getType()) {
            case INPUT_STRING :
                InputString inputString = (InputString)input;
                String output = mainController.runCommand(inputString.getString());
                userOutputWriter.printOutput(output);
                break;
            case INPUT_OPERATION :
                throw new UnsupportedOperationException("Scroll is not supported yet.");
        }
    }
    
    public boolean isReadyToExit() {
        return mainController.isReadyToExit();
    }
}
