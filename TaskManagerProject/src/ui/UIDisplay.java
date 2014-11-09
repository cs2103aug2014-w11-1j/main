package ui;

import java.io.IOException;

import jline.ArgumentCompletor;
import jline.ConsoleReader;
import main.MainController;
import ui.input.Input;
import ui.input.InputOperation;
import ui.input.InputString;

//@author A0113011L
public class UIDisplay {
    private UserInputReader userInputReader;
    private UserOutputWriter userOutputWriter;
    
    private final MainController mainController;


    public UIDisplay(MainController mainController, ArgumentCompletor completor)  {
        this.mainController = mainController;
        try {
            ConsoleReader reader = new ConsoleReader();
            reader.clearScreen();
            userInputReader = new UserInputReader(reader, completor);
            userOutputWriter = new UserOutputWriter(reader);
            
            String header = "Welcome to Taskline.";
            String lines = mainController.runCommand("report");
            userOutputWriter.printOutput(header, lines);
        } catch(IOException e) {
            System.out.println("IOException : " + e.getMessage());
        }
    }
    
    /**
     * Called from main
     */
    public void commandLoopIteration() {
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
                String header = ">" + inputString.getString();
                userOutputWriter.printOutput(header, output);
                break;
            case INPUT_OPERATION :
                InputOperation inputOperation = (InputOperation)input;
                switch (inputOperation.getOperation()) {
                    case SCROLL_UP :
                        userOutputWriter.scrollUp();
                        break;
                    case SCROLL_DOWN :
                        userOutputWriter.scrollDown();
                        break;
                    case PREV_COMMAND :
                        userOutputWriter.prevCommand();
                        break;
                    case NEXT_COMMAND :
                        userOutputWriter.nextCommand();
                        break;
                }
        }
    }
    
    public boolean isReadyToExit() {
        return mainController.isReadyToExit();
    }
}
