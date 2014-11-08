package manager;

import io.IFileInputOutput;
import main.command.alias.IAliasStorage;
import manager.datamanager.AddManager;
import manager.datamanager.AliasManager;
import manager.datamanager.DeleteManager;
import manager.datamanager.EditManager;
import manager.datamanager.FreeDaySearchManager;
import manager.datamanager.ReportManager;
import manager.datamanager.SearchManager;
import manager.datamanager.UndoManager;
import manager.datamanager.freetimemanager.FreeTimeSearchManager;
import data.TaskData;

/**
 * A class which packages all the managers. It is passed into a Command object
 * to allow the command to act on the managers.
 */
//@author A0065475X
public class ManagerHolder {
    private final StateManager stateManager;
    private final AddManager addManager;
    private final SearchManager searchManager;
    private final EditManager editManager;
    private final DeleteManager deleteManager;
    private final UndoManager undoManager;
    private final FreeDaySearchManager freeDaySearchManager;
    private final FreeTimeSearchManager freeTimeSearchManager;
    private final AliasManager aliasManager;
    private final ReportManager reportManager;


    public ManagerHolder(TaskData taskData, IFileInputOutput fileInputOutput, 
            IAliasStorage aliasStorage, IFileInputOutput aliasFileInputOutput) {
        
        addManager = new AddManager(taskData);
        searchManager = new SearchManager(taskData);
        editManager = new EditManager(taskData);
        deleteManager = new DeleteManager(taskData);
        undoManager = new UndoManager(taskData);
        freeDaySearchManager = new FreeDaySearchManager(taskData);
        freeTimeSearchManager = new FreeTimeSearchManager(taskData);
        reportManager = new ReportManager(taskData);
        aliasManager = new AliasManager(aliasStorage, taskData);
        
        stateManager = new StateManager(fileInputOutput, aliasFileInputOutput,
                undoManager, searchManager);
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
    
    public FreeTimeSearchManager getFreeTimeSearchManager() {
    	return freeTimeSearchManager;
    }

    public ReportManager getReportManager() {
        return reportManager;
    }

    public AliasManager getAliasManager() {
        return aliasManager;
    }
}
