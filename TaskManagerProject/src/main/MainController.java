package main;

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
        throw new UnsupportedOperationException("Not implemented yet");
    }
    
    public boolean isReadyToExit() {
        return readyToExit;
    }
}
