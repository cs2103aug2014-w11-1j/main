package main.command;

import manager.ManagerHolder;
import manager.result.Result;
import manager.result.SimpleResult;

//@author A0065475X
public class BackCommand extends Command {
    
    public BackCommand (ManagerHolder managerHolder) {
        super(managerHolder);
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
