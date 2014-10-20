package main.command;

import main.response.Response;
import manager.ManagerHolder;
import manager.result.Result;


public class ArgumentCommand extends TargetedCommand {

    public ArgumentCommand(String args, ManagerHolder managerHolder) {
        super(managerHolder);
        parse(args);
    }

    private void parse(String args) {
        String remaining = tryParseIdsIntoSet(args);
        if (remaining.length() > 0) {
            targetTaskIdSet = null;
        }
    }
    
    @Override
    public Response execute() {
        if (!isValidArguments()) {
            return updateInvalidCommand();
        }
        
        if (!isCommandAllowed()) {
            return updateCannotExecuteCommand();
        }

        TargetedCommand command = stateManager.retrieveStoredCommand();
        command.setTargets(targetTaskIdSet);
        
        return command.execute();
    }

    @Override
    protected boolean isValidArguments() {
        return targetTaskIdSet != null;
    }

    @Override
    protected boolean isCommandAllowed() {
        return stateManager.isWaitingForArguments();
    }

    @Override
    protected Result executeAction() {
        assert false : "This method is not supported.";
        throw new UnsupportedOperationException("This method is not supported.");
    }
    
}
