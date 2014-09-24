package main.command;

import main.response.CannotExecuteCommandResponse;
import main.response.Response;
import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.AddManager;
import manager.result.Result;
import data.taskinfo.TaskInfo;

public class AddCommand implements Command {
    private final AddManager addManager;
    private final StateManager stateManager;
    private final TaskInfo taskToAdd;

    public AddCommand(String args, ManagerHolder managerHolder) {
        taskToAdd = parse(args);
        addManager = managerHolder.getAddManager();
        stateManager = managerHolder.getStateManager();
    }

    private TaskInfo parse(String args) {
        return new TaskInfo();
    }

    @Override
    public Response execute() {
        if (stateManager.canAdd()) {
        	stateManager.enterAddMode();
            Result result = addManager.addTask(taskToAdd);
            Response response = stateManager.update(result);
            return response;
        } else {
            return new CannotExecuteCommandResponse();
        }
    }

}
