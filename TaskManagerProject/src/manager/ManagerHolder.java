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
        stateManager = new StateManager();
        addManager = new AddManager(fileInputOutput, taskData);
        searchManager = new SearchManager(fileInputOutput, taskData);
        editManager = new EditManager(fileInputOutput, taskData);
        deleteManager = new DeleteManager(fileInputOutput, taskData);
        undoManager = new UndoManager(fileInputOutput, taskData);
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
