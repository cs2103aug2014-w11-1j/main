package manager.datamanager;

import io.FileInputOutput;
import manager.result.AddResult;
import manager.result.Result;
import manager.result.SimpleResult;
import data.TaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;

public class AddManager extends AbstractManager {
	
	private TaskId id = null;
    
    public AddManager(FileInputOutput fileInputOutput, TaskData taskData) {
        super(fileInputOutput, taskData);
    }

    public Result addTask(TaskInfo taskInfo) {
    	
    	if (taskInfo == null){
    		return new SimpleResult(Result.Type.ADD_FAILURE);
    	}
    	
    	readFromFile();
    	
    	id = taskData.add(taskInfo);
    	
    	if (id == null){
    		return new SimpleResult(Result.Type.ADD_FAILURE);
    	}
    	
    	writeToFile();
    	
    	return new AddResult(Result.Type.ADD_SUCCESS, taskInfo, id);
    	
    }
    
}
