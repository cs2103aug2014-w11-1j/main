package main.command;

import main.message.EnumMessage;
import main.modeinfo.EmptyModeInfo;
import main.response.Response;
import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.DeleteManager;
import manager.datamanager.SearchManager;
import manager.result.Result;
import data.TaskId;

public class DeleteCommand implements Command {
    private final DeleteManager deleteManager;
    private final SearchManager searchManager;
    private final StateManager stateManager;
    private final TaskId taskId;

    public DeleteCommand(String args, ManagerHolder managerHolder) {
        deleteManager = managerHolder.getDeleteManager();
        searchManager = managerHolder.getSearchManager();
        stateManager = managerHolder.getStateManager();
        
        taskId = parse(args);
    }

    private TaskId parse(String args) {
        try {
            int relativeTaskId = Integer.parseInt(args);
            return searchManager.getAbsoluteIndex(relativeTaskId);
        } catch (NumberFormatException e) {
            String absoluteTaskId = args;
            return TaskId.makeTaskId(absoluteTaskId);
        }
    }

    @Override
    public Response execute() {
        if (stateManager.canDelete()) {
            stateManager.beforeCommandExecutionUpdate();

            Result result = deleteManager.deleteTask(taskId);
            Response response = stateManager.update(result);
            return response;
        } else {
            EnumMessage message = EnumMessage.cannotExecuteCommand();
            EmptyModeInfo modeInfo = new EmptyModeInfo();
            return new Response(message, modeInfo);
        }
    }

}
