package manager;

import main.response.Response;
import manager.datamanager.UndoManager;
import manager.result.Result;

public class StateManager {
    private final UndoManager undoManager;

    public StateManager(UndoManager undoManager) {
        this.undoManager = undoManager;
    }

    public boolean canAdd() {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
    
    public boolean canSearch() {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
    
    public boolean canEdit() {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
    
    public boolean canDelete() {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }

    public boolean canUndo() {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
    
    /**
     * Updates the program's state using the result obtained from the managers.
     * @param result
     * @return
     */
    public Response update(Result result) {
        
        undoManager.retrieveUndoSnapshot();
        throw new UnsupportedOperationException("Not Implemented Yet");    
    }
}
