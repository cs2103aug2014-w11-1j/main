package taskline.debug;

import io.FileInputOutput;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.MainController;
import manager.ManagerHolder;
import ui.debug.UIDisplay;
import data.TaskData;

/**
 * Does the main initialization of the program structure.
 * 
 * @author Oh
 */
public class Taskline {
    
    public static void main(String[] args) {
        setupLogger();
        
        String fileName = "tasks.txt";
        
        TaskData taskData = new TaskData();
        FileInputOutput fileInputOutput = new FileInputOutput(taskData, fileName); 
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

    public static void setupLogger() {
        Logger log = Logger.getLogger("TEA");  
        log.setUseParentHandlers(false);
        FileHandler fileHandler;  

        try {  
            fileHandler = new FileHandler("taskline.log", 1000000, 1, true);
            SimpleFormatter formatter = new SimpleFormatter();  
            fileHandler.setFormatter(formatter);  
            
            log.addHandler(fileHandler);
            log.setLevel(Level.FINEST);
            
        } catch (SecurityException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }

        log.log(Level.FINE, "TIME : " + System.currentTimeMillis());
        log.log(Level.SEVERE, "ORANGE LOGGIGN LOGGING");
    }
}
