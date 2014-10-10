package main.command;

import main.response.Response;
import manager.ManagerHolder;
import manager.StateManager;
import manager.result.Result;
import manager.result.SimpleResult;

public class BackCommand extends Command {
    private final StateManager stateManager;

    public BackCommand (ManagerHolder managerHolder) {
        super(managerHolder);
        stateManager = managerHolder.getStateManager();
    }

    @Override
    protected boolean isValidArguments() {
        return true;
    }

    @Override
    protected boolean isCommandAllowed() {
        return stateManager.canGoBack();
    }

    @Override
    protected Result executeAction() {
        Result result = new SimpleResult(Result.Type.GO_BACK);
        return result;
    }
}
