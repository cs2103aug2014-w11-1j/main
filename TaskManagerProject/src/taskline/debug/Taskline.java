package taskline.debug;

import io.AliasFileInputOutput;
import io.FileInputOutput;
import io.IFileInputOutput;
import main.MainController;
import main.command.alias.AliasStorage;
import main.command.alias.IAliasStorage;
import manager.ManagerHolder;
import taskline.TasklineLogger;
import ui.debug.UIDisplay;
import data.TaskData;

/**
 * Does the main initialization of the program structure.<br>
 * <br>
 * How to put a logger in your class:<br>
 * private static final Logger log = Logger.getLogger(Taskline.LOGGER_NAME);
 *
 * @author Oh
 */
public class Taskline {

    public static void main(String[] args) {
        TasklineLogger.setupLogger();

        String fileName = "tasks.txt";
        String aliasFileName = "alias.txt";

        AliasStorage aliasStorage = new AliasStorage();
        IFileInputOutput aliasFileInputOutput =
                new AliasFileInputOutput(aliasStorage, aliasFileName);

        TaskData taskData = new TaskData();
        IFileInputOutput fileInputOutput =
                new FileInputOutput(taskData, fileName);
        
        ManagerHolder managerHolder = new ManagerHolder(taskData,
                fileInputOutput, aliasStorage, aliasFileInputOutput);
        MainController mainController = new MainController(managerHolder,
                aliasStorage, aliasFileInputOutput);
        
        UIDisplay uiDisplay = new UIDisplay(mainController);

        startCommandLoop(uiDisplay);
        TasklineLogger.closeLoggerFileHandler();
    }

    private static void startCommandLoop(UIDisplay uiDisplay) {
        while (!uiDisplay.isReadyToExit()) {
            uiDisplay.commandLoopIteration();
        }
    }
}
