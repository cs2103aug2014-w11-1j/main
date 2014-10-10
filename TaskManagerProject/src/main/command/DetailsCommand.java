package main.command;

import main.message.EnumMessage;
import main.modeinfo.EmptyModeInfo;
import main.response.Response;
import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.SearchManager;
import manager.result.Result;
import manager.result.SimpleResult;
import data.TaskId;

public class DetailsCommand extends Command {
    private final StateManager stateManager;
    private final SearchManager searchManager;
    
    private final TaskId taskId;

    public DetailsCommand(String args, ManagerHolder managerHolder) {
        stateManager = managerHolder.getStateManager();
        searchManager = managerHolder.getSearchManager();
        
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
        
        if (stateManager.canSearch()) {
            stateManager.beforeCommandExecutionUpdate();

            Result result = searchManager.details(taskId);
            Response response = stateManager.update(result);
            return response;
        } else {
            EnumMessage message = EnumMessage.cannotExecuteCommand();
            EmptyModeInfo modeInfo = new EmptyModeInfo();
            return new Response(message, modeInfo);
        }
    }

}
