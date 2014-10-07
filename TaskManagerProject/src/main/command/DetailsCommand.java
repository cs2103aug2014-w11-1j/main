package main.command;

import data.TaskId;
import main.message.EnumMessage;
import main.modeinfo.EmptyModeInfo;
import main.response.Response;
import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.SearchManager;
import manager.result.Result;

public class DetailsCommand implements Command {
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
            return searchManager.getAbsoluteIndex(relativeTaskId);
        } catch (NumberFormatException e) {
            String absoluteTaskId = args;
            return TaskId.makeTaskId(absoluteTaskId);
        }
    }

    @Override
    public Response execute() {
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
