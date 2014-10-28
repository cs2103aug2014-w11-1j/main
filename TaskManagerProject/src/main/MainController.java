package main;

import java.util.logging.Level;
import java.util.logging.Logger;

import main.command.Command;
import main.command.CommandController;
import main.command.alias.AliasStorage;
import main.formatting.Formatter;
import main.response.Response;
import manager.ManagerHolder;
import taskline.debug.Taskline;

/**
 *
 * @author You Jun
 */
public class MainController {
    private static final Logger log = Logger.getLogger(Taskline.LOGGER_NAME);

    private final ManagerHolder managerHolder;
    private final AliasStorage aliasStorage;
    private boolean readyToExit;

    public MainController(ManagerHolder managerHolder, AliasStorage aliasStorage) {
        this.managerHolder = managerHolder;
        this.aliasStorage = aliasStorage;
    }

    public String runCommand(String commandString) {
        if (commandString.trim().isEmpty()) {
            return "";
        }

        CommandController commandController = new CommandController(managerHolder, aliasStorage);
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
