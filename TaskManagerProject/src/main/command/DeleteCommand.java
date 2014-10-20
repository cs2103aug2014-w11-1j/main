package main.command;

import manager.ManagerHolder;
import manager.datamanager.DeleteManager;
import manager.result.Result;

public class DeleteCommand extends TargetedCommand {
    private final DeleteManager deleteManager;

    public DeleteCommand(String args, ManagerHolder managerHolder) {
        super(managerHolder);
        deleteManager = managerHolder.getDeleteManager();
        
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
