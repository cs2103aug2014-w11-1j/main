package main.command;
import main.message.EnumMessage;
import main.modeinfo.EmptyModeInfo;
import main.response.Response;
import manager.ManagerHolder;
import manager.StateManager;
import manager.result.Result;
import manager.result.SimpleResult;

//@author A0065475X
public abstract class Command {
    protected final StateManager stateManager;
    
    public Command(ManagerHolder managerHolder) {
        stateManager = managerHolder.getStateManager();
    }


    public Response execute() {
        if (!isValidArguments()) {
            return updateInvalidArguments();
        }
        
        if (isCommandAllowed()) {
            stateManager.beforeCommandExecutionUpdate();

            Result result = executeAction();
            Response response = stateManager.update(result);
            return response;
            
        } else {
            return updateCannotExecuteCommand();
        }
    }

    protected Response updateInvalidArguments() {
        Result result = new SimpleResult(Result.Type.INVALID_ARGUMENT);
        Response response = stateManager.update(result);
        return response;
    }

    protected Response updateCannotExecuteCommand() {
        EnumMessage message = EnumMessage.cannotExecuteCommand();
        EmptyModeInfo modeInfo = new EmptyModeInfo();
        return new Response(message, modeInfo);
    }

    protected Response updateInvalidCommand() {
        Result result = new SimpleResult(Result.Type.INVALID_COMMAND);
        Response response = stateManager.update(result);
        return response;
    }
    
    protected abstract boolean isValidArguments();
    protected abstract boolean isCommandAllowed();
    protected abstract Result executeAction();
}