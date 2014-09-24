package manager.datamanager;

import io.FileInputOutput;
import manager.result.DeleteResult;
import manager.result.Result;
import manager.result.SimpleResult;
import data.TaskData;
import data.TaskId;

public class DeleteManager extends AbstractManager {

    public DeleteManager(FileInputOutput fileInputOutput, TaskData taskData) {
        super(fileInputOutput, taskData);
    }

    public Result deleteTask(TaskId taskId) {
    	
    	if (taskId == null){
    		return new SimpleResult(Result.Type.DELETE_FAILURE);
    	}
    	
    	readFromFile();
    	
    	Boolean isSuccessful = taskData.remove(taskId);
    	
    	writeToFile();
    	
    	if (isSuccessful){
    		return new DeleteResult(Result.Type.DELETE_SUCCESS, taskId);
    	}
    	
    	return new SimpleResult(Result.Type.DELETE_FAILURE);
    }
       
}
