package manager;

import io.FileInputOutput;
import main.modeinfo.SearchModeInfo;
import manager.datamanager.SearchManager;
import manager.datamanager.UndoManager;
import manager.datamanager.searchfilter.Filter;
import manager.result.SearchResult;
import data.TaskId;
import data.taskinfo.TaskInfo;

public class UpdateManager {


    private final FileInputOutput fileInputOutput;
	private final UndoManager undoManager;
	private final SearchManager searchManager;
	
	public UpdateManager(FileInputOutput fileInputOutput, UndoManager undoManager, SearchManager searchManager){
		this.fileInputOutput = fileInputOutput;
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
        return fileInputOutput.write();
    }  
	
	
}
