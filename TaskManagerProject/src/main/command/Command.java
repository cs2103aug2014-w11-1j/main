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
            stateManager.beforeCommandUpdate();

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
    
    /**
     * If the parsing parsed something invalid, (e.g. delete with no tasks
     * specified), make this return false. This will cause the program to
     * print an "invalid arguments" message to the user.
     * @return true iff the arguments parse into something that's valid.
     */
    protected abstract boolean isValidArguments();
    /**
     * This is the check with the StateManager for clearance to use the
     * command.<br>
     * e.g. for add command, this will look like:<br>
     * return stateManager.canAdd();
     * @return true iff StateManager approves the execution of the command.
     */
    protected abstract boolean isCommandAllowed();
    /**
     * The command's functionality should be written here. Make the necessary
     * calls to the managers involved here. Get the execution result from the
     * manager.
     * @return a result, which will automatically be used to update the
     * StateManager.
     */
    protected abstract Result executeAction();
}