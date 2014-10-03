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
        CommandController cC = new CommandController(managerHolder);
        Command c = cC.getCommand(commandString);
        Response r = c.execute();
        Formatter formatter = new Formatter();
        return formatter.format(r);
        //throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public boolean isReadyToExit() {
        return readyToExit;
    }
}
