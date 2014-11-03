package taskline;

import io.FileInputOutput;
import io.IFileInputOutput;

import java.io.IOException;

import main.MainController;
import main.command.alias.AliasStorage;
import main.command.alias.IAliasStorage;
import manager.ManagerHolder;

import org.fusesource.jansi.AnsiConsole;
import static org.fusesource.jansi.Ansi.*;
import static org.fusesource.jansi.Ansi.Color.*;

import ui.UIDisplay;
import data.TaskData;

/**
 * Does the main initialization of the program structure.
 *
 * @author Oh
 */
public class Taskline {

    public static void main(String[] args) throws IOException {
        AnsiConsole.systemInstall();
        TasklineLogger.setupLogger();
        String fileName = "tasks.txt";

        String aliasFileName = "alias.txt";
        IAliasStorage aliasStorage = new AliasStorage();

        TaskData taskData = new TaskData();
        IFileInputOutput fileInputOutput = new FileInputOutput(taskData, fileName);
        ManagerHolder managerHolder = new ManagerHolder(taskData, fileInputOutput, aliasStorage);
        MainController mainController = new MainController(managerHolder, aliasStorage);
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
