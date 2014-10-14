package manager.datamanager;

import main.command.TaskIdSet;
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

    public Result deleteTask(TaskIdSet taskIdSet) {
    	
        boolean allSuccessful = true;
        TaskId returnTaskId = null;
        TaskInfo returnTaskInfo = null;
        
        for (TaskId taskId : taskIdSet) {
        	if (taskId == null){
        	    allSuccessful = false;
        	    break;
        	}

            returnTaskId = taskId;
        	returnTaskInfo = taskData.getTaskInfo(taskId);
        	boolean isSuccessful = taskData.remove(taskId);
        	
        	if (!isSuccessful){
                allSuccessful = false;
                break;
        	}
        }
        
        if (allSuccessful) {
            return new DeleteResult(Result.Type.DELETE_SUCCESS,
                    returnTaskId, returnTaskInfo);
        } else {
            taskData.reverseLastChange();
            return new SimpleResult(Result.Type.DELETE_FAILURE);
        }
    }
       
}
