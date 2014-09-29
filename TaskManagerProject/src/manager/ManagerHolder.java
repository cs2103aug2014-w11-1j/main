package manager;

import io.FileInputOutput;
import manager.datamanager.AddManager;
import manager.datamanager.DeleteManager;
import manager.datamanager.EditManager;
import manager.datamanager.SearchManager;
import manager.datamanager.UndoManager;
import data.TaskData;

public class ManagerHolder {
    private StateManager stateManager;
    private AddManager addManager;
    private SearchManager searchManager;
    private EditManager editManager;
    private DeleteManager deleteManager;
    private UndoManager undoManager;
    

    public ManagerHolder(TaskData taskData, FileInputOutput fileInputOutput) {
        addManager = new AddManager(taskData);
        searchManager = new SearchManager(taskData);
        editManager = new EditManager(taskData);
        deleteManager = new DeleteManager(taskData);
        undoManager = new UndoManager(taskData);
        stateManager = new StateManager(fileInputOutput, undoManager, searchManager);
    }

    public StateManager getStateManager() {
        return stateManager;
    }
    
    public AddManager getAddManager() {
        return addManager;
    }
    
    public SearchManager getSearchManager() {
        return searchManager;
    }
    
    public EditManager getEditManager() {
        return editManager;
    }
    
    public DeleteManager getDeleteManager() {
        return deleteManager;
    }
    
    public UndoManager getUndoManager() {
        return undoManager;
    }
}
