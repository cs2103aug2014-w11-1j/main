package manager;

import io.IFileInputOutput;
import main.modeinfo.SearchModeInfo;
import manager.datamanager.SearchManager;
import manager.datamanager.UndoManager;
import manager.datamanager.searchfilter.Filter;
import manager.result.SearchResult;
import data.TaskId;
import data.taskinfo.TaskInfo;

//@author A0119432L
public class UpdateManager {

    private final IFileInputOutput aliasFileInputOutput;
    private final IFileInputOutput fileInputOutput;
	private final UndoManager undoManager;
	private final SearchManager searchManager;
	
	public UpdateManager(IFileInputOutput fileInputOutput,
	        IFileInputOutput aliasFileInputOutput, UndoManager undoManager,
	        SearchManager searchManager){
	    
        this.fileInputOutput = fileInputOutput;
        this.aliasFileInputOutput = aliasFileInputOutput;
		this.undoManager = undoManager;
		this.searchManager = searchManager;	
	}

	public void redoSearch(Filter[] filters){
	    assert filters != null : "Can't search with null filters!";
		searchManager.searchTasks(filters);
	}
	
	public SearchModeInfo getSearchModeInfo(){
		SearchResult redoSearchResult = searchManager.getLastSearchResult();
        TaskInfo[] tasks = redoSearchResult.getTasks();
        TaskId[] taskIds = redoSearchResult.getTaskIds();
        String[] suggestions = redoSearchResult.getSuggestions();
        SearchModeInfo searchModeInfo = new SearchModeInfo(tasks, taskIds, 
                suggestions);
        return searchModeInfo;
	}
	
	public TaskInfo getTaskInfo(TaskId taskId){
		return searchManager.getTaskInfo(taskId);
	}
	
	public void updateUndoHistory(){
		undoManager.updateUndoHistory();
	}
	
	public void preExecutionCheck(){
		 boolean fileChanged = readFromFile();
	        
        if (fileChanged) {
            undoManager.clearHistory();
        }
    }

	public boolean readFromFile() {
        return fileInputOutput.read();
    }

	public boolean writeToFile() {
	    boolean result1 = aliasFileInputOutput.write();
	    boolean result2 = fileInputOutput.write();
	    
        return result1 && result2;
    }  
	
	
}
