package main.command;

import manager.ManagerHolder;
import manager.result.Result;
import manager.result.SimpleResult;

public class ExitCommand extends Command {

    public ExitCommand(ManagerHolder managerHolder) {
        super(managerHolder);
    }

    @Override
    protected boolean isValidArguments() {
        return true;
    }

    @Override
    protected boolean isCommandAllowed() {
        return stateManager.canExit();
    }

    @Override
    protected Result executeAction() {
        return new SimpleResult(Result.Type.EXIT);
    }

}
