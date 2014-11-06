package taskline.debug;

import java.util.logging.Level;
import java.util.logging.Logger;

import jline.SimpleCompletor;
import io.AliasFileInputOutput;
import io.FileInputOutput;
import io.IFileInputOutput;
import main.MainController;
import main.command.alias.AliasStorage;
import main.command.alias.IAliasStorage;
import manager.ManagerHolder;
import taskline.TasklineLogger;
import ui.debug.UIDisplay;
import data.AutoCompleteDictionary;
import data.TaskData;

/**
 * Does the main initialization of the program structure.<br>
 * <br>
 * How to put a logger in your class:<br>
 * private static final Logger log = Logger.getLogger(Taskline.LOGGER_NAME);
 *
 */
//@author A0065475X
public class Taskline {

    public static void main(String[] args) {
        TasklineLogger.setupLogger();

        String fileName = "tasks.txt";
        String aliasFileName = "alias.txt";

        SimpleCompletor simpleCompletor = new SimpleCompletor(new String[]{});  
        AutoCompleteDictionary autoCompleteDictionary =
                new AutoCompleteDictionary(simpleCompletor);
        
        AliasStorage aliasStorage = new AliasStorage();
        IFileInputOutput aliasFileInputOutput = new AliasFileInputOutput(
                aliasStorage, aliasFileName, autoCompleteDictionary);

        TaskData taskData = new TaskData();
        IFileInputOutput fileInputOutput =
                new FileInputOutput(taskData, fileName);
        
        ManagerHolder managerHolder = new ManagerHolder(taskData,
                fileInputOutput, aliasStorage, aliasFileInputOutput);
        MainController mainController = new MainController(managerHolder,
                aliasStorage, aliasFileInputOutput);
        
        UIDisplay uiDisplay = new UIDisplay(mainController);

        startCommandLoopWithLogger(uiDisplay);
    }

    private static void startCommandLoopWithLogger(UIDisplay uiDisplay) {
        Logger log = TasklineLogger.getLogger();
        try {
            startCommandLoop(uiDisplay);
        } catch (Exception e) {
            log.log(Level.SEVERE, e.getMessage(), e);
            throw e;
        } finally {
            TasklineLogger.closeLoggerFileHandler();
        }
    }

    private static void startCommandLoop(UIDisplay uiDisplay) {
        while (!uiDisplay.isReadyToExit()) {
            uiDisplay.commandLoopIteration();
        }
    }
}
