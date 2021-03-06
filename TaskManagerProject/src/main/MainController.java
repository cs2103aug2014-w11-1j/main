package main;

import io.IFileInputOutput;

import java.util.logging.Level;
import java.util.logging.Logger;

import main.command.Command;
import main.command.CommandController;
import main.command.alias.IAliasStorage;
import main.formatting.Formatter;
import main.response.Response;
import manager.ManagerHolder;
import taskline.TasklineLogger;

//@author A0111862M
/**
 * The main controller that links the UI to the rest of the program.
 * Controls the main logic of the program - the command execution process.
 */
public class MainController {
    private static final Logger log = TasklineLogger.getLogger();

    private final ManagerHolder managerHolder;
    private final IAliasStorage aliasStorage;
    private final IFileInputOutput aliasFileInputOutput;
    private boolean readyToExit;

    public MainController(ManagerHolder managerHolder,
            IAliasStorage aliasStorage, IFileInputOutput aliasFileInputOutput) {
        this.managerHolder = managerHolder;
        this.aliasStorage = aliasStorage;
        this.aliasFileInputOutput = aliasFileInputOutput;
    }

    public String runCommand(String commandString) {
        if (commandString.trim().isEmpty()) {
            return "";
        }

        CommandController commandController = new CommandController(
                managerHolder, aliasStorage, aliasFileInputOutput);
        Command curCommand = commandController.getCommand(commandString);
        log.log(Level.FINE, "Execute Command: " + curCommand.getClass().getName());
        Response curResponse = curCommand.execute();

        if (curResponse.isExitResponse()) {
            setReadyToExit();
        }
        Formatter formatter = new Formatter();
        return formatter.format(curResponse);
    }

    public boolean isReadyToExit() {
        return readyToExit;
    }

    private void setReadyToExit() {
        readyToExit = true;
    }
}
