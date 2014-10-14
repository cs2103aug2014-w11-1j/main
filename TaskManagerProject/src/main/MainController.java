package main;

import java.util.logging.Level;
import java.util.logging.Logger;

import main.command.Command;
import main.command.CommandController;
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
    private boolean readyToExit;
    
    public MainController(ManagerHolder managerHolder) {
        this.managerHolder = managerHolder;
    }

    public String runCommand(String commandString) {
        CommandController commandController = new CommandController(managerHolder);
        Command curCommand = commandController.getCommand(commandString);
        log.log(Level.FINE, "Execute Command: " + curCommand.getClass().getName());
        Response curResponse = curCommand.execute();
        Formatter formatter = new Formatter();
        return formatter.format(curResponse);
        //throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public boolean isReadyToExit() {
        return readyToExit;
    }
}
