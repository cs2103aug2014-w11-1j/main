package main.command;

import main.response.Response;
import manager.ManagerHolder;
import manager.StateManager;
import manager.result.Result;
import manager.result.SimpleResult;

public class BackCommand implements Command {
    private final StateManager stateManager;

    public BackCommand (ManagerHolder managerHolder) {
        stateManager = managerHolder.getStateManager();
    }

    @Override
    public Response execute() {
        Result result = new SimpleResult(Result.Type.GO_BACK);
        Response response = stateManager.update(result);
        return response;
    }
}
