package main;

import main.command.Command;
import main.command.CommandController;
import main.formatting.Formatter;
import main.response.Response;
import manager.ManagerHolder;

/**
 * 
 * @author You Jun
 */
public class MainController {
    
    private final ManagerHolder managerHolder;
    private boolean readyToExit;
    
    public MainController(ManagerHolder managerHolder) {
        this.managerHolder = managerHolder;
    }

    public String runCommand(String commandString) {
        CommandController commandController = new CommandController(managerHolder);
        Command curCommand = commandController.getCommand(commandString);
        Response curResponse = curCommand.execute();
        Formatter formatter = new Formatter();
        return formatter.format(curResponse);
        //throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public boolean isReadyToExit() {
        return readyToExit;
    }
}
