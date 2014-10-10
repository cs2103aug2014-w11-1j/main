package main.command;

import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.DeleteManager;
import manager.datamanager.SearchManager;
import manager.result.Result;
import data.TaskId;

public class DeleteCommand extends Command {
    private final DeleteManager deleteManager;
    private final SearchManager searchManager;
    private final StateManager stateManager;
    private final TaskId taskId;

    public DeleteCommand(String args, ManagerHolder managerHolder) {
        super(managerHolder);
        deleteManager = managerHolder.getDeleteManager();
        searchManager = managerHolder.getSearchManager();
        stateManager = managerHolder.getStateManager();
        
        taskId = parse(args);
    }

    private TaskId parse(String args) {
        return parseTaskId(args);
    }

    @Override
    protected boolean isValidArguments() {
        return taskId != null;
    }

    @Override
    protected boolean isCommandAllowed() {
        return stateManager.canDelete();
    }

    @Override
    protected Result executeAction() {
        Result result = deleteManager.deleteTask(taskId);
        return result;
    }

}
