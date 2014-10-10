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

public abstract class Command {
    private final SearchManager searchManager;
    private final StateManager stateManager;
    
    public Command(ManagerHolder managerHolder) {
        searchManager = managerHolder.getSearchManager();
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
    
    public void setTargetSet(){}
    protected TaskId parseBatchId(){return null;}
    
    protected abstract boolean isValidArguments();
    protected abstract boolean isCommandAllowed();
    protected abstract Result executeAction();


    /**
     * 
     * @param args
     * @return null iff invalid task Id.
     */
    protected TaskId parseTaskId(String args) {
        assert args != null : "There should not be a null passed in.";
        if (args.isEmpty()) {
            return null;
        }
        
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
}