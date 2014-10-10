package main.command;

import main.response.Response;
import manager.ManagerHolder;
import manager.StateManager;
import manager.result.Result;
import manager.result.SimpleResult;

public class InvalidCommand extends Command {
    private final StateManager stateManager;

    public InvalidCommand(ManagerHolder managerHolder) {
        stateManager = managerHolder.getStateManager();
    }

    @Override
    public Response execute() {
        Result result = new SimpleResult(Result.Type.INVALID_COMMAND);
        Response response = stateManager.update(result);
        return response;
    }
}
