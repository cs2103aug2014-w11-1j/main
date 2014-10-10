package main.command;

import manager.ManagerHolder;
import manager.StateManager;
import manager.result.Result;
import manager.result.SimpleResult;

public class InvalidCommand extends Command {
    private final StateManager stateManager;

    public InvalidCommand(ManagerHolder managerHolder) {
        super(managerHolder);
        stateManager = managerHolder.getStateManager();
    }

    
    @Override
    protected boolean isValidArguments() {
        return true;
    }

    @Override
    protected boolean isCommandAllowed() {
        return true;
    }

    @Override
    protected Result executeAction() {
        Result result = new SimpleResult(Result.Type.INVALID_COMMAND);
        return result;
    }
}
