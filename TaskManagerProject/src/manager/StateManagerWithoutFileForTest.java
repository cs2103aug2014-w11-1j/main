package manager;

import io.FileInputOutput;
import main.message.AddSuccessfulMessage;
import main.message.DeleteSuccessfulMessage;
import main.message.EditSuccessfulMessage;
import main.message.EnumMessage;
import main.message.EnumMessage.MessageType;
import main.modeinfo.EditModeInfo;
import main.modeinfo.EmptyModeInfo;
import main.modeinfo.SearchModeInfo;
import main.response.Response;
import manager.datamanager.SearchManager;
import manager.datamanager.UndoManager;
import manager.result.AddResult;
import manager.result.DeleteResult;
import manager.result.EditResult;
import manager.result.Result;
import manager.result.Result.Type;
import manager.result.SearchResult;
import data.TaskId;

public class StateManagerWithoutFileForTest {



private final UndoManager undoManager;
private final SearchManager searchManager;
private State currentState;
private TaskId editingTaskId;
private Response response;

	public enum State {
	    AVAILABLE,      // Normal state
	    EDIT_MODE,      // Can edit the same task without re-specifying task ID
	    SEARCH_MODE,    // In search mode, searchAgain is called after each command
	    LOCKED_MODE     // In locked mode, no modifying data is allowed
	}

	public StateManagerWithoutFileForTest( UndoManager undoManager, SearchManager searchManager) {
		this.undoManager = undoManager;
		this.searchManager = searchManager;
		this.currentState = State.AVAILABLE;
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
			editingTaskId = null;
			return true;
		}else{
			return false;
		}
	}

	public boolean enterSearchMode(){
		if (currentState != State.AVAILABLE){
			return false;
		}else{
			setState(State.SEARCH_MODE);
			return true;
		}
	}
	
	public boolean exitSearchMode(){
		if (currentState == State.SEARCH_MODE){
			setState(State.AVAILABLE);
			return true;
		}else{
			return false;
		}
	}
	/**
	 * Updates the program's state using the result obtained from the managers.
	 * @param result result returned from the manager
	 * @return response generated accordingly
	 */
	public Response update(Result result) {
        
        undoManager.updateUndoHistory();
        
        response = null;     
        response = generateResponse(result);
    
        searchModeCheck(result);
        
        searchModeEnterCheck(result);

        if (response != null) {
            return response;
        } else {
            throw new UnsupportedOperationException("Not Implemented Yet");    
        }
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

	/**
	 * This method is to handling Search Mode.
	 * If any command is executed, except a search command again under search mode, 
	 * update the SearchModeInfo by redoLastSearch and store it back to response
	 */
	private void searchModeCheck(Result result) {
		if ((inState(State.SEARCH_MODE)) && (result.getType() != Type.SEARCH_SUCCESS)){
        	SearchResult redoSearchResult = (SearchResult)searchManager.redoLastSearch();;
        	SearchModeInfo searchModeInfo = 
        			new SearchModeInfo(redoSearchResult.getTasks(),redoSearchResult.getTaskIds());
        		
        	response = new Response(response.getMessage(), searchModeInfo);
        	}
	}

	
	/**
	 * This method is to generate different response according to the 
	 * type of result.
	 * @param result result to be converted
	 * @return response generated
	 */
	private Response generateResponse(Result result) {
		switch (result.getType()){
                         
            case EDIT_MODE_END :
            	exitEditMode();
            	break;
            case SEARCH_MODE_END : 
            	exitSearchMode();
            case ADD_SUCCESS :
            	 AddResult addResult = (AddResult)result;
                 AddSuccessfulMessage addSuccessMessage = 
                         new AddSuccessfulMessage(addResult.getTaskInfo(),
                                 addResult.getTaskId());
                EmptyModeInfo addSuccessModeInfo = new EmptyModeInfo();
            	response = new Response(addSuccessMessage, addSuccessModeInfo);
            	break;
            case ADD_FAILURE : 
                EnumMessage addFailMessage = new EnumMessage(EnumMessage.MessageType.ADD_FAILED);
                EmptyModeInfo addFailModeInfo = new EmptyModeInfo();
                response = new Response(addFailMessage, addFailModeInfo);
                break;
            case DELETE_SUCCESS :
            	DeleteResult deleteResult = (DeleteResult)result;
                DeleteSuccessfulMessage deleteSuccessMessage = 
                		new DeleteSuccessfulMessage(deleteResult.getTaskInfo(), 
                		        deleteResult.getTaskId());
                EmptyModeInfo deleteSuccessModeInfo = new EmptyModeInfo();
                response = new Response(deleteSuccessMessage, deleteSuccessModeInfo);
                break;
            case DELETE_FAILURE : 
                EnumMessage deleteFailMessage = new EnumMessage(MessageType.EDIT_FAILED);
                EmptyModeInfo deleteFailModeInfo = new EmptyModeInfo();
                response = new Response(deleteFailMessage, deleteFailModeInfo);
                break;
            case EDIT_SUCCESS : 
                EditResult editResult = (EditResult)result;
                enterEditMode(editResult.getTaskId());
            	EditSuccessfulMessage editSuccessMessage = 
                        new EditSuccessfulMessage(editResult.getTaskInfo(), editingTaskId, null);
                EditModeInfo editSuccessModeInfo = new EditModeInfo(editingTaskId);
                response = new Response(editSuccessMessage, editSuccessModeInfo);
                break;
            case EDIT_FAILURE : 
                EnumMessage editFailMessage = new EnumMessage(MessageType.EDIT_FAILED);
            	EditModeInfo editFailModeInfo = new EditModeInfo(editingTaskId);
            	response = new Response(editFailMessage, editFailModeInfo);
            	break;
            case TAG_ADD_SUCCESS : 
            	EditResult tagAddResult = (EditResult)result;
            	EditSuccessfulMessage tagAddSuccessfulMessage = 
            			new EditSuccessfulMessage(tagAddResult.getTaskInfo(),
            					tagAddResult.getTaskId(), tagAddResult.getChangedFields());
            	EditModeInfo tagAddModeInfo = new EditModeInfo(tagAddResult.getTaskId());
            	response = new Response(tagAddSuccessfulMessage, tagAddModeInfo);
            	break;
            case TAG_ADD_FAILURE:
            	EnumMessage tagAddFailMessage = new EnumMessage(MessageType.ADD_TAG_FAILED);
            	EditModeInfo tagAddFailModeInfo = new EditModeInfo(editingTaskId);
            	response = new Response(tagAddFailMessage, tagAddFailModeInfo);
            	break;
            case TAG_DELETE_SUCCESS : 
            	EditResult tagDeleteResult = (EditResult)result;
            	EditSuccessfulMessage tagDeleteSuccessfulMessage = 
            			new EditSuccessfulMessage(tagDeleteResult.getTaskInfo(),
            					tagDeleteResult.getTaskId(), tagDeleteResult.getChangedFields());
            	EditModeInfo tagDeleteModeInfo = new EditModeInfo(tagDeleteResult.getTaskId());
            	response = new Response(tagDeleteSuccessfulMessage, tagDeleteModeInfo);
            	break;
            case TAG_DELETE_FAILURE :
            	EnumMessage tagDeleteFailMessage = new EnumMessage(MessageType.ADD_TAG_FAILED);
            	EditModeInfo tagDeleteFailModeInfo = new EditModeInfo(editingTaskId);
            	response = new Response(tagDeleteFailMessage, tagDeleteFailModeInfo);
            	break;
            case SEARCH_SUCCESS : 
            	SearchResult searchResult = (SearchResult)result;
            	EnumMessage searchSuccessMessage = new EnumMessage(MessageType.SEARCH_SUCCESS);
            	SearchModeInfo searchModeInfo = new SearchModeInfo(searchResult.getTasks(), searchResult.getTaskIds());
            	response = new Response(searchSuccessMessage, searchModeInfo);
            	break;
            case SEARCH_FAILURE : 
            	EnumMessage searchFailMessage = new EnumMessage(MessageType.SEARCH_FAILED);
            	EmptyModeInfo searchFailModeInfo = new EmptyModeInfo();
            	response = new Response(searchFailMessage, searchFailModeInfo);
            	break;
            default:
                break;
        }
		return response;
	}


}
