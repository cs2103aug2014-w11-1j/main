package manager;

import io.FileInputOutput;

import java.util.logging.Level;
import java.util.logging.Logger;

import main.command.TargetedCommand;
import main.command.TaskIdSet;
import main.message.AddSuccessfulMessage;
import main.message.DeleteSuccessfulMessage;
import main.message.DetailsMessage;
import main.message.EditSuccessfulMessage;
import main.message.EnumMessage;
import main.message.EnumMessage.MessageType;
import main.message.Message;
import main.modeinfo.EditModeInfo;
import main.modeinfo.EmptyModeInfo;
import main.modeinfo.ModeInfo;
import main.modeinfo.SearchModeInfo;
import main.response.Response;
import manager.datamanager.SearchManager;
import manager.datamanager.UndoManager;
import manager.result.AddResult;
import manager.result.DeleteResult;
import manager.result.DetailsResult;
import manager.result.EditResult;
import manager.result.Result;
import manager.result.Result.Type;
import manager.result.StartEditModeResult;
import taskline.debug.Taskline;
import data.TaskId;
import data.taskinfo.TaskInfo;


/**
 * This is a state manager that keep track of the availability 
 * of executing a certain command.
 * It can also update the state by giving a result generated by managers
 * and return a response to command.
 * @author BRUCE
 *
 */
public class StateManager {
    private static final Logger log = Logger.getLogger(Taskline.LOGGER_NAME);

	private State currentState;
	private TaskIdSet editingTaskIdSet;
	private UpdateManager updateManager;
	private TargetedCommand savedCommand;
	
	public enum State {
	    AVAILABLE,      // Normal state
	    EDIT_MODE,      // Can edit the same task without re-specifying task ID
	    SEARCH_MODE,    // In search mode, searchAgain is called after each command
        WAITING_MODE,     // In waiting mode, a command is waiting for arguments before execution.
        LOCKED_MODE     // In locked mode, no modifying data is allowed
	}

	public StateManager(FileInputOutput fileInputOutput, UndoManager undoManager, SearchManager searchManager) {
		this.currentState = State.AVAILABLE;	
		this.updateManager = new UpdateManager(fileInputOutput, undoManager, searchManager);
	}

    public boolean canExit() {
        return true;
    }


	public boolean canAdd() {
		return true;
	    //return currentState == State.AVAILABLE;
	}

	public boolean canSearch() {
        return true;
        //return currentState == State.AVAILABLE;
	}

	public boolean canEdit() {
        return true;
        //return currentState == State.AVAILABLE || currentState == State.EDIT_MODE;
	}

	public boolean canDelete() {
        return true;
        //return currentState == State.AVAILABLE || currentState == State.EDIT_MODE;
	}

	public boolean canUndo() {
        return true;
        //return currentState == State.AVAILABLE;
	}

    public boolean canGoBack() {
        return inState(State.EDIT_MODE) || inState(State.SEARCH_MODE) ||
                inState(State.WAITING_MODE);
    }
    
    public boolean inEditMode() {
        return inState(State.EDIT_MODE);
    }
    
    public boolean canQuerySearchManager() {
        return inState(State.SEARCH_MODE) || inState(State.WAITING_MODE);
    }
	
	private void setState(State newState) {
	    currentState = newState;
	}
	
	private boolean inState(State state) {
	    assert currentState != null : "State cannot be null";
	    return (currentState == state);
	}
    
	private boolean enterEditMode(TaskIdSet idSet) {
	    if (inState(State.EDIT_MODE)) {
	        return false;
	    }
	    
		setState(State.EDIT_MODE);
		editingTaskIdSet = idSet;
		return true;
	}
	
	private boolean exitEditMode() {
		if (inState(State.EDIT_MODE)){
			setState(State.AVAILABLE);
			editingTaskIdSet = null;
			return true;
		} else {
			return false;
		}
	}

	private boolean enterSearchMode() {
	    if (inState(State.WAITING_MODE)) {
	        return false;
	    }
        if (inState(State.SEARCH_MODE)) {
            return false;
        }
        
		setState(State.SEARCH_MODE);
		return true;
	}
	
	private boolean exitSearchMode() {
		if (inState(State.SEARCH_MODE)){
			setState(State.AVAILABLE);
			return true;
		}else{
			return false;
		}
	}
	
    
    /**
     * This method is called just before every command execution.
     */
    public void beforeCommandExecutionUpdate() {
       updateManager.preExecutionCheck();
    }

    
    public boolean isWaitingForArguments() {
        return inState(State.WAITING_MODE);
    }
    
    public TargetedCommand retrieveStoredCommand() {
        assert savedCommand != null;
        assert inState(State.WAITING_MODE);
        
        return getAndClearSavedCommand();
    }
    
	/**
	 * Updates the program's state using the result obtained from the managers.
	 * @param result result returned from the manager
	 * @return response generated accordingly
	 */
	public Response update(Result result) {
        getAndClearSavedCommand();
        
		return processResult(result);
    }

    public Response updateAndStoreCommand(Result result, TargetedCommand command) {
        getAndClearSavedCommand();
        
        setSavedCommand(command);
        return processResult(result);
    }

    protected Response processResult(Result result) {
        log.log(Level.FINER, "Apply StateManager update - Result = " + 
                result.getType().name() + " / " + result.getClass().getName());
        
        updateManager.updateUndoHistory();
        
        Message message = applyResult(result);
        ModeInfo modeInfo = generateModeInfo(result);
        
        updateManager.writeToFile();
        
        searchModeCheck(result);
        
        searchModeEnterCheck(result);

        postUpdateLog(message, modeInfo);
        return new Response(message, modeInfo);
    }

    private boolean setSavedCommand(TargetedCommand command) {
        assert savedCommand == null;
        assert !inState(State.WAITING_MODE);
        
        this.savedCommand = command;
        setState(State.WAITING_MODE);
        return true;
    }
    
    private TargetedCommand getAndClearSavedCommand() {
        if (inState(State.WAITING_MODE)) {
            TargetedCommand temp = savedCommand;
            savedCommand = null;
            setState(State.AVAILABLE);
            return temp;
        } else {
            assert savedCommand == null;
            return null;
        }
    }
	
	
    private void postUpdateLog(Message message, ModeInfo modeInfo) {
        log.log(Level.FINE, "StateManager updated. Current State: " + currentState.name());
        log.log(Level.FINER, "Return response. Message = " +
                message.getType().name() + " / " +
                message.getClass().getName() + ". ModeInfo = " + 
                modeInfo.getType().name() + " / " + 
                modeInfo.getClass().getName());
    }

	/**
	 * This method is to check whether the state should enter search mode
	 * if a search is successfully executed, enter search mode
	 * @param result result to be checked
	 */
	private void searchModeEnterCheck(Result result) {
		if (result.getType() == Type.SEARCH_SUCCESS){
        	enterSearchMode();
        }
	}
	
	private ModeInfo generateModeInfo(Result result) {
	    switch (currentState) {
	        case AVAILABLE :
	            return new EmptyModeInfo();

            case SEARCH_MODE :
                return searchModeCheck(result);
                
            case EDIT_MODE :
                return editModeCheck(result);
                
            case WAITING_MODE :
                SearchModeInfo modeInfo = searchModeCheck(result);
                modeInfo.makeIntoWaitingModeInfo();
                return modeInfo;
                
            case LOCKED_MODE :
                return new EmptyModeInfo();
                
            default :
                throw new UnsupportedOperationException("Unknown state: " +
                            currentState.name());
	    }
	}

	/**
	 * This method is to handling Search Mode.
	 * If any command is executed, except a search command again under search mode, 
	 * update the SearchModeInfo by redoLastSearch and store it back to response
	 */
	private SearchModeInfo searchModeCheck(Result result) {
		if (result.getType() != Type.SEARCH_SUCCESS) {
			updateManager.redoSearch();
		}
		
		return updateManager.getSearchModeInfo();
	}
	
	private ModeInfo editModeCheck(Result result) {
//	    TaskInfo taskInfo = searchManager.getTaskInfo(editingTaskId);
	    TaskId taskId = editingTaskIdSet.iterator().next();
	    
		TaskInfo taskInfo = updateManager.getTaskInfo(taskId);
        return new EditModeInfo(taskInfo, taskId);
	}

	
	/**
	 * This method is to generate different response according to the 
	 * type of result.
	 * @param result result to be converted
	 * @return response generated
	 */
	private Message applyResult(Result result) {
		switch (result.getType()){
            case EXIT :
                if (inState(State.EDIT_MODE)) {
                    exitEditMode();
                } else if (inState(State.SEARCH_MODE)) {
                    exitSearchMode();
                }
                return new EnumMessage(EnumMessage.MessageType.EXIT);
                
            case ADD_SUCCESS :
            	 AddResult addResult = (AddResult)result;
                 AddSuccessfulMessage addSuccessMessage = 
                         new AddSuccessfulMessage(addResult.getTaskInfo(),
                                 addResult.getTaskId());
                 return addSuccessMessage;

            case ADD_FAILURE : 
                return new EnumMessage(EnumMessage.MessageType.ADD_FAILED);
                
            case DELETE_SUCCESS :
            	DeleteResult deleteResult = (DeleteResult)result;
                DeleteSuccessfulMessage deleteSuccessMessage = 
                		new DeleteSuccessfulMessage(deleteResult.getTaskInfo(), 
                		        deleteResult.getTaskId());
                return deleteSuccessMessage;
                
            case DELETE_FAILURE : 
                return new EnumMessage(MessageType.DELETE_FAILED);
                
            case EDIT_MODE_START : {
                StartEditModeResult editResult = (StartEditModeResult)result;
                enterEditMode(editResult.getTaskIdSet());
                return new EnumMessage(MessageType.EDIT_STARTED);
            }

            case EDIT_MODE_END :
                exitEditMode();
                return new EnumMessage(EnumMessage.MessageType.EDIT_ENDED);

            case SEARCH_MODE_END : 
                exitSearchMode();
                return new EnumMessage(EnumMessage.MessageType.SEARCH_ENDED);
                
            case GO_BACK :
                if (inState(State.EDIT_MODE)) {
                    exitEditMode();
                    return new EnumMessage(EnumMessage.MessageType.EDIT_ENDED);
                } else if (inState(State.SEARCH_MODE)) {
                    exitSearchMode();
                    return new EnumMessage(EnumMessage.MessageType.SEARCH_ENDED);
                }

            case EDIT_SUCCESS : 
                EditResult editResult = (EditResult)result;
            	EditSuccessfulMessage editSuccessMessage = 
                        new EditSuccessfulMessage(editResult.getTaskInfo(), editResult.getTaskId(), editResult.getChangedFields());
            	return editSuccessMessage;

            case EDIT_FAILURE : 
                return new EnumMessage(MessageType.EDIT_FAILED);
            	
            case TAG_ADD_SUCCESS : 
            	EditResult tagAddResult = (EditResult)result;
            	EditSuccessfulMessage tagAddSuccessfulMessage = 
            			new EditSuccessfulMessage(tagAddResult.getTaskInfo(),
            					tagAddResult.getTaskId(), tagAddResult.getChangedFields());
            	return tagAddSuccessfulMessage;

            case TAG_ADD_FAILURE:
            	return new EnumMessage(MessageType.ADD_TAG_FAILED);

            case TAG_DELETE_SUCCESS : 
            	EditResult tagDeleteResult = (EditResult)result;
            	EditSuccessfulMessage tagDeleteSuccessfulMessage = 
            			new EditSuccessfulMessage(tagDeleteResult.getTaskInfo(),
            					tagDeleteResult.getTaskId(), tagDeleteResult.getChangedFields());
            	return tagDeleteSuccessfulMessage;
            	
            case TAG_DELETE_FAILURE :
            	return new EnumMessage(MessageType.ADD_TAG_FAILED);

            case SEARCH_SUCCESS : 
            	//SearchResult searchResult = (SearchResult)result;
                enterSearchMode();
            	return new EnumMessage(MessageType.SEARCH_SUCCESS);
            	//SearchModeInfo searchModeInfo = new SearchModeInfo(searchResult.getTasks(), searchResult.getTaskIds());
            	//response = new Response(searchSuccessMessage, searchModeInfo);
                
            case SEARCH_FAILURE : 
                return new EnumMessage(MessageType.SEARCH_FAILED);
                
            case UNDO_SUCCESS : 
                return new EnumMessage(MessageType.UNDO_SUCCESS);
                
            case UNDO_FAILURE : 
                return new EnumMessage(MessageType.UNDO_FAILED);
                
            case REDO_SUCCESS : 
                return new EnumMessage(MessageType.REDO_SUCCESS);
                
            case REDO_FAILURE : 
                return new EnumMessage(MessageType.REDO_FAILED);

            case INVALID_COMMAND : 
                return new EnumMessage(MessageType.INVALID_COMMAND);

            case INVALID_ARGUMENT : 
                return new EnumMessage(MessageType.INVALID_ARGUMENT);
                
            case DETAILS :
                DetailsResult detailsResult = (DetailsResult)result;
                return new DetailsMessage(detailsResult.getTask(),
                        detailsResult.getTaskId());

            default:
                throw new UnsupportedOperationException("Unknown state: " +
                            result.getType().name());
        }
	}


}
