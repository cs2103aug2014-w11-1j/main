package taskline;

import io.FileInputOutput;

import java.io.IOException;

import main.MainController;
import main.command.alias.AliasStorage;
import manager.ManagerHolder;
import ui.UIDisplay;
import data.TaskData;

/**
 * Does the main initialization of the program structure.
 *
 * @author Oh
 */
public class Taskline {

    public static void main(String[] args) throws IOException {
        //setupLogger();
        String fileName = "tasks.txt";

        String aliasFileName = "alias.txt";
        AliasStorage aliasStorage = new AliasStorage(aliasFileName);

        TaskData taskData = new TaskData();
        FileInputOutput fileInputOutput = new FileInputOutput(taskData, fileName);
        ManagerHolder managerHolder = new ManagerHolder(taskData, fileInputOutput);
        MainController mainController = new MainController(managerHolder, aliasStorage);
        UIDisplay uiDisplay = new UIDisplay(mainController);

        startCommandLoop(uiDisplay);
    }

    private static void startCommandLoop(UIDisplay uiDisplay) {
        while (!uiDisplay.isReadyToExit()) {
            uiDisplay.commandLoopIteration();
        }
    }

    /*public static void setupLogger() {
        Logger log = Logger.getLogger("ASDDSAD");
        log.setUseParentHandlers(false);
        FileHandler fileHandler;

        try {
            fileHandler = new FileHandler("taskline.log", 1000000, 1, true);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            log.addHandler(fileHandler);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info("Test log.");

        log.log(Level.ALL, "GIGIGI LOGGING");
    }*/
}
