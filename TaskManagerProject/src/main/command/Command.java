package main.command;
import main.message.EnumMessage;
import main.modeinfo.EmptyModeInfo;
import main.response.Response;
import manager.ManagerHolder;
import manager.StateManager;
import manager.result.Result;
import manager.result.SimpleResult;
import data.TaskId;

public abstract class Command {
    private final StateManager stateManager;
    
    public Command(ManagerHolder managerHolder) {
        stateManager = managerHolder.getStateManager();
    }


    public final Response execute() {
        if (!isValidArguments()) {
            Result result = new SimpleResult(Result.Type.INVALID_ARGUMENT);
            Response response = stateManager.update(result);
            return response;
        }
        
        if (isCommandAllowed()) {
            stateManager.beforeCommandExecutionUpdate();

            Result result = executeAction();
            Response response = stateManager.update(result);
            return response;
            
        } else {
            EnumMessage message = EnumMessage.cannotExecuteCommand();
            EmptyModeInfo modeInfo = new EmptyModeInfo();
            return new Response(message, modeInfo);
        }
    }
    
    protected abstract boolean isValidArguments();
    protected abstract boolean isCommandAllowed();
    protected abstract Result executeAction();
}