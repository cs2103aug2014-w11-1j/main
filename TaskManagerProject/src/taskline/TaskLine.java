package taskline;

import io.FileInputOutput;
import main.MainController;
import manager.ManagerHolder;
import ui.UIDisplay;
import data.TaskData;

/**
 * Does the main initialization of the program structure.
 * 
 * @author Oh
 */
public class TaskLine {
    
    public static void main(String[] args) {
        
        TaskData taskData = new TaskData();
        FileInputOutput fileInputOutput = new FileInputOutput(); 
        ManagerHolder managerHolder = new ManagerHolder(taskData, fileInputOutput);
        MainController mainController = new MainController(managerHolder);
        UIDisplay uiDisplay = new UIDisplay(mainController);
        
        startCommandLoop(uiDisplay);
    }

    private static void startCommandLoop(UIDisplay uiDisplay) {
        while (!uiDisplay.isReadyToExit()) {
            uiDisplay.commandLoopIteration();
        }
    }
}
