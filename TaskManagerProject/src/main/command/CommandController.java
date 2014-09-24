package main.command;

import manager.ManagerHolder;

/**
 * Handles the parsing of commands.
 * Commands are returned to MainController for execution.
 * 
 * @author You Jun
 */
public class CommandController {
    private final ManagerHolder managerHolder;
    
    public CommandController(ManagerHolder managerHolder) {
        this.managerHolder = managerHolder;
    }
}
