package main.command;

import main.message.EnumMessage;
import main.modeinfo.EmptyModeInfo;
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
        TaskInfo newTask = CommandParser.parseTask(args);
        return newTask;
    }

    @Override
    public Response execute() {
        if (stateManager.canAdd()) {
            stateManager.beforeCommandExecutionUpdate();

            Result result = addManager.addTask(taskToAdd);
            Response response = stateManager.update(result);
            return response;
        } else {
            EnumMessage message = EnumMessage.cannotExecuteCommand();
            EmptyModeInfo modeInfo = new EmptyModeInfo();
            return new Response(message, modeInfo);
        }
    }

}
