package taskline.debug;

import io.FileInputOutput;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import main.MainController;
import main.command.alias.AliasData;
import manager.ManagerHolder;
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
    private static final String LOGGER_FILENAME = "taskline.log";
    public static String LOGGER_NAME = "Taskline";

    private static FileHandler loggerFileHandler;

    public static void main(String[] args) {
        setupLogger();

        String fileName = "tasks.txt";

        TaskData taskData = new TaskData();
        AliasData aliasData = new AliasData();
        FileInputOutput fileInputOutput = new FileInputOutput(taskData, fileName);
        ManagerHolder managerHolder = new ManagerHolder(taskData, fileInputOutput);
        MainController mainController = new MainController(managerHolder);
        UIDisplay uiDisplay = new UIDisplay(mainController);

        startCommandLoop(uiDisplay);
        closeLoggerFileHandler();
    }

    private static void startCommandLoop(UIDisplay uiDisplay) {
        while (!uiDisplay.isReadyToExit()) {
            uiDisplay.commandLoopIteration();
        }
    }

    public static void setupLogger() {
        Logger log = Logger.getLogger(LOGGER_NAME);
        log.setUseParentHandlers(false);

        try {
            Path path = Paths.get(LOGGER_FILENAME + ".lck");
            Files.deleteIfExists(path);

            loggerFileHandler = new FileHandler(LOGGER_FILENAME, true);
            SimpleFormatter formatter = new SimpleFormatter();
            loggerFileHandler.setFormatter(formatter);

            log.addHandler(loggerFileHandler);
            log.setLevel(Level.FINEST);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.log(Level.INFO, "Taskline initialised.");
    }

    private static void closeLoggerFileHandler() {
        if (loggerFileHandler != null) {
            loggerFileHandler.close();
        }
    }
}
