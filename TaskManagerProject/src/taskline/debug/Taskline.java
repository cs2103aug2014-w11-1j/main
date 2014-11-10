package taskline.debug;

import io.AliasFileInputOutput;
import io.FileInputOutput;
import io.IFileInputOutput;

import java.util.logging.Level;
import java.util.logging.Logger;

import jline.SimpleCompletor;
import main.MainController;
import main.command.alias.AliasStorage;
import manager.ManagerHolder;
import taskline.TasklineLogger;
import ui.debug.UIDisplay;
import data.AutoCompleteDictionary;
import data.TaskData;

//@author A0065475X
/**
 * Does the main initialization of the program structure.<br>
 * <br>
 * How to put a logger in your class:<br>
 * private static final Logger log = Logger.getLogger(Taskline.LOGGER_NAME);
 *
 */
public class Taskline {

    public static void main(String[] args) {
        TasklineLogger.setupLogger(args);

        String fileName = "tasks.txt";
        String aliasFileName = "alias.txt";

        MainController mainController = setupTaskLine(fileName, aliasFileName);
        
        UIDisplay uiDisplay = new UIDisplay(mainController);

        startCommandLoopWithLogger(uiDisplay);
    }

    public static MainController setupTaskLine(String fileName,
            String aliasFileName) {
        
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
        return mainController;
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
