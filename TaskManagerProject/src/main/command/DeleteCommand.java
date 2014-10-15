package main.command;

import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.DeleteManager;
import manager.result.Result;

public class DeleteCommand extends TargetedCommand {
    private final DeleteManager deleteManager;
    private final StateManager stateManager;

    public DeleteCommand(String args, ManagerHolder managerHolder) {
        super(managerHolder);
        deleteManager = managerHolder.getDeleteManager();
        stateManager = managerHolder.getStateManager();
        
        parse(args);
    }

    private void parse(String args) {
        String remaining = tryParseIdsIntoSet(args);
        if (remaining.length() > 0) {
            targetTaskIdSet = null;
        }
        if (targetTaskIdSet == null) {
            parseAsSearchString(args);
        }
    }
    
    @Override
    protected boolean isValidArguments() {
        return targetTaskIdSet != null;
    }

    @Override
    protected boolean isCommandAllowed() {
        return stateManager.canDelete();
    }

    @Override
    protected Result executeAction() {
        Result result = deleteManager.deleteTask(targetTaskIdSet);
        return result;
    }

}
