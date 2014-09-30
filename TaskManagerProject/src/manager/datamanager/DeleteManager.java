package manager.datamanager;

import io.FileInputOutput;
import manager.result.DeleteResult;
import manager.result.Result;
import manager.result.SimpleResult;
import data.TaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;


/**
 * This is a delete manager that enables deletion of a task with specified
 * taskId inside the taskData.
 * @author BRUCE
 *
 */
public class DeleteManager extends AbstractManager {

    public DeleteManager(TaskData taskData) {
        super(taskData);
    }

    public Result deleteTask(TaskId taskId) {
    	
    	if (taskId == null){
    		return new SimpleResult(Result.Type.DELETE_FAILURE);
    	}
    	
    	
    	TaskInfo taskInfo = taskData.getTaskInfo(taskId);
    	Boolean isSuccessful = taskData.remove(taskId);
    	
    	if (isSuccessful){
    		return new DeleteResult(Result.Type.DELETE_SUCCESS, taskId, taskInfo);
    	}
    	
    	return new SimpleResult(Result.Type.DELETE_FAILURE);
    }
       
}
