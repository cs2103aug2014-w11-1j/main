package manager;

import main.response.Response;
import manager.datamanager.UndoManager;
import manager.result.Result;

public class StateManager {

	private final UndoManager undoManager;

	// Availability of command. True for able, false for unable
	private boolean addCheck;
	private boolean deleteCheck;
	private boolean searchCheck;
	private boolean undoCheck;
	private boolean editCheck;

	public StateManager(UndoManager undoManager) {
		this.undoManager = undoManager;
		addCheck = true;
		deleteCheck = true;
		searchCheck = true;
		undoCheck = true;
		editCheck = true;
	}

	public boolean canAdd() {
		return addCheck;
	}

	public boolean canSearch() {
		return searchCheck;
	}

	public boolean canEdit() {
		return editCheck;
	}

	public boolean canDelete() {
		return deleteCheck;
	}

	public boolean canUndo() {
		return undoCheck;
	}

	public void enterAddMode() {
		addCheck = false;
	}

	public void enterSearchMode() {
		searchCheck = false;
	}

	public void enterEditMode() {
		editCheck = false;
	}

	public void enterDeleteMode() {
		deleteCheck = false;
	}

	public void enterUndoMode() {
		undoCheck = false;
	}

	public void exitAddMode() {
		addCheck = true;
	}

	public void exitSearchMode() {
		searchCheck = true;
	}

	public void exitDeleteMode() {
		searchCheck = true;
	}

	public void exitUndoMode() {
		undoCheck = true;
	}

	public void exitEditMode() {
		editCheck = true;
	}

	/**
	 * Updates the program's state using the result obtained from the managers.
	 * 
	 * @param result
	 * @return
	 */
	public Response update(Result result) {
        
        undoManager.retrieveUndoSnapshot();
        
        switch (result.getType()){
        case ADD_SUCCESS: exitEditMode();
        			 //return
        		     break;
        case ADD_FAILURE: exitEditMode();
        		     //return
        		     break;
        case DELETE_SUCCESS: exitDeleteMode();
        		     //return
        		     break;
        case DELETE_FAILURE: exitDeleteMode();
        		     //return
        		     break;
        default: break;
        }
        

        
        throw new UnsupportedOperationException("Not Implemented Yet");    
    }
}
