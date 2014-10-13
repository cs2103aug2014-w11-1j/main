package manager;

import data.TaskId;
import data.taskinfo.TaskInfo;
import io.FileInputOutput;
import main.modeinfo.ModeInfo;
import main.modeinfo.SearchModeInfo;
import manager.datamanager.SearchManager;
import manager.datamanager.UndoManager;
import manager.result.SearchResult;

public class UpdateManager {


    private final FileInputOutput fileInputOutput;
	private final UndoManager undoManager;
	private final SearchManager searchManager;
	
	public UpdateManager(FileInputOutput fileInputOutput, UndoManager undoManager, SearchManager searchManager){
		this.fileInputOutput = fileInputOutput;
		this.undoManager = undoManager;
		this.searchManager = searchManager;	
	}

	public void redoSearch(){
		searchManager.redoLastSearch();
	}
	
	public ModeInfo getSearchModeInfo(){
		SearchResult redoSearchResult = searchManager.getLastSearchResult();
        TaskInfo[] tasks = redoSearchResult.getTasks();
        TaskId[] taskIds = redoSearchResult.getTaskIds();
        SearchModeInfo searchModeInfo = new SearchModeInfo(tasks, taskIds);
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
	            undoManager.clearUndoHistory();
	        }
	    }

	public boolean readFromFile() {
	        return fileInputOutput.read();
	    }

	public boolean writeToFile() {
	        return fileInputOutput.write();
	    }  
	
	
}
