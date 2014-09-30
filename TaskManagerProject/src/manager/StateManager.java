package manager;

import io.FileInputOutput;
import main.message.AddSuccessfulMessage;
import main.message.DeleteSuccessfulMessage;
import main.message.EditSuccessfulMessage;
import main.message.EnumMessage;
import main.message.EnumMessage.MessageType;
import main.modeinfo.EmptyModeInfo;
import main.response.Response;
import manager.datamanager.SearchManager;
import manager.datamanager.UndoManager;
import manager.result.AddResult;
import manager.result.DeleteResult;
import manager.result.EditResult;
import manager.result.Result;
import data.TaskId;


/**
 * This is a state manager that keep track of the availability 
 * of executing a certain command.
 * It can also update the state by giving a result generated by managers
 * and return a response to command.
 * @author BRUCE
 *
 */
public class StateManager {

    private final FileInputOutput fileInputOutput;
	private final UndoManager undoManager;
	private final SearchManager searchManager;
	private State currentState;
	private TaskId editingTaskId;

	public enum State {
	    AVAILABLE,      // Normal state
	    EDIT_MODE,      // Can edit the same task without re-specifying task ID
	    SEARCH_MODE,    // In search mode, searchAgain is called after each command
	    LOCKED_MODE     // In locked mode, no modifying data is allowed
	}

	public StateManager(FileInputOutput fileInputOutput, UndoManager undoManager, SearchManager searchManager) {
	    this.fileInputOutput = fileInputOutput;
		this.undoManager = undoManager;
		this.searchManager = searchManager;
	}

	public boolean canAdd() {
		return currentState == State.AVAILABLE;
	}

	public boolean canSearch() {
        return currentState == State.AVAILABLE;
	}

	public boolean canEdit() {
        return currentState == State.AVAILABLE || currentState == State.EDIT_MODE;
	}

	public boolean canDelete() {
        return currentState == State.AVAILABLE || currentState == State.EDIT_MODE;
	}

	public boolean canUndo() {
        return currentState == State.AVAILABLE;
	}
	
	private void setState(State newState) {
	    currentState = newState;
	}
	
	public boolean inState(State state) {
	    return (currentState == state);
	}
    
	public boolean enterEditMode(TaskId id){
		if (currentState != State.AVAILABLE){
			return false;
		}else{
			setState(State.EDIT_MODE);
			editingTaskId = id;
			return true;
		}
	}
	
	public boolean exitEditMode(){
		if (currentState == State.EDIT_MODE){
			setState(State.AVAILABLE);
			return true;
		}else{
			return false;
		}
	}

	/**
	 * Updates the program's state using the result obtained from the managers.
	 * 
	 * @param result
	 * @return
	 */
	public Response update(Result result) {
        
        undoManager.retrieveUndoSnapshot();
        
        Response response = null;
        
        // generate response
        switch (result.getType()){
            case EDIT_MODE_START :
                setState(State.EDIT_MODE);
                break;
                
            case EDIT_MODE_END :
                setState(State.AVAILABLE);
                break;
            case ADD_SUCCESS :
                AddSuccessfulMessage addSuccessMessage = 
                        new AddSuccessfulMessage(((AddResult)result).getTaskInfo());
                EmptyModeInfo addSuccessModeInfo = new EmptyModeInfo();
            	response = new Response(addSuccessMessage, addSuccessModeInfo);
            case ADD_FAILURE : 
                EnumMessage addFailMessage = new EnumMessage(EnumMessage.MessageType.ADD_FAILED);
                EmptyModeInfo addFailModeInfo = new EmptyModeInfo();
                response = new Response(addFailMessage, addFailModeInfo);
            case DELETE_SUCCESS :
                DeleteSuccessfulMessage deleteSuccessMessage = 
                		new DeleteSuccessfulMessage(((DeleteResult)result).getTaskInfo());
                EmptyModeInfo deleteSuccessModeInfo = new EmptyModeInfo();
                response = new Response(deleteSuccessMessage, deleteSuccessModeInfo);
                break;
            case DELETE_FAILURE : 
                EnumMessage deleteFailMessage = new EnumMessage(MessageType.EDIT_FAILED);
                EmptyModeInfo deleteFailModeInfo = new EmptyModeInfo();
                response = new Response(deleteFailMessage, deleteFailModeInfo);
            case EDIT_SUCCESS : 
                EditSuccessfulMessage editSuccessMessage = 
                        new EditSuccessfulMessage(((EditResult)result).getTaskInfo(), editingTaskId, null);
                EmptyModeInfo editSuccessModeInfo = new EmptyModeInfo();
                response = new Response(editSuccessMessage, editSuccessModeInfo);
            case EDIT_FAILURE : 
                EnumMessage editFailMessage = new EnumMessage(MessageType.EDIT_FAILED);
            	EmptyModeInfo editFailModeInfo = new EmptyModeInfo();
            	response = new Response(editFailMessage, editFailModeInfo);
            default:
                break;
        }
        
         writeToFile();
        
        // handling search mode: search again after each successful command
        if (inState(State.SEARCH_MODE)){
        	switch (result.getType()){
        		case DELETE_SUCCESS : 
        			searchManager.searchAgain();
        			break;
        		case EDIT_SUCCESS : 
        			searchManager.searchAgain();
        			break;
        		default :
        			break;
        	}
        }

        if (response != null) {
            return response;
        } else {
            throw new UnsupportedOperationException("Not Implemented Yet");    
        }
    }

    
    /**
     * This method is called just before every command execution.
     */
    public void beforeCommandExecutionUpdate() {
        boolean fileChanged = readFromFile();
        
        if (fileChanged) {
            undoManager.clearUndoHistory();
        }
    }

    private boolean readFromFile() {
        return fileInputOutput.read();
    }

    private boolean writeToFile() {
        return fileInputOutput.write();
    }
}
