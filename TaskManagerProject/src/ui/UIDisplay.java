package ui;

import main.MainController;

public class UIDisplay {
    private UserInputReader userInputReader;
    private UserOutputWriter userOutputWriter;
    
    private final MainController mainController;
    
    public UIDisplay(MainController mainController) {
        this.mainController = mainController;
    }
    
    /**
     * Called from main
     */
    public void commandLoopIteration() {
        
    }
    
    public boolean isReadyToExit() {
        return mainController.isReadyToExit();
    }
}
