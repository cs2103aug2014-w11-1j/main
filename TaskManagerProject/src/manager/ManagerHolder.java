package manager;

import io.FileInputOutput;
import manager.datamanager.AddManager;
import manager.datamanager.DeleteManager;
import manager.datamanager.EditManager;
import manager.datamanager.FreeDaySearchManager;
import manager.datamanager.ReportManager;
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
    private FreeDaySearchManager freeDaySearchManager;
    private ReportManager reportManager;
    

    public ManagerHolder(TaskData taskData, FileInputOutput fileInputOutput) {
        addManager = new AddManager(taskData);
        searchManager = new SearchManager(taskData);
        editManager = new EditManager(taskData);
        deleteManager = new DeleteManager(taskData);
        undoManager = new UndoManager(taskData);
        freeDaySearchManager = new FreeDaySearchManager(taskData);
        reportManager = new ReportManager(taskData);
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
    
    public FreeDaySearchManager getFreeDaySearchManager() {
        return freeDaySearchManager;
    }
    
    public ReportManager getReportManager() {
        return reportManager;
    }
}
