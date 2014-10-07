package main.command;

import main.message.EnumMessage;
import main.modeinfo.EmptyModeInfo;
import main.response.Response;
import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.DeleteManager;
import manager.datamanager.SearchManager;
import manager.result.Result;
import manager.result.SimpleResult;
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
            if (stateManager.inSearchMode()) {
                return searchManager.getAbsoluteIndex(relativeTaskId);
            } else {
                return null;
            }
        } catch (NumberFormatException e) {
            String absoluteTaskId = args;
            return TaskId.makeTaskId(absoluteTaskId);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public Response execute() {
        if (taskId == null) {
            Result result = new SimpleResult(Result.Type.INVALID_ARGUMENT);
            Response response = stateManager.update(result);
            return response;
        }
        
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
