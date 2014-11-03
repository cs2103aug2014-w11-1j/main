package manager.datamanager;

import java.util.ArrayList;
import java.util.List;

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

//@author A0119432L
public class DeleteManager extends AbstractManager {

    public DeleteManager(TaskData taskData) {
        super(taskData);
    }

    public Result deleteTask(TaskIdSet taskIdSet) {
    	
        boolean allSuccessful = true;
        
        List<TaskId> idList = new ArrayList<TaskId>();
        List<TaskInfo> taskList = new ArrayList<TaskInfo>();
        
        for (TaskId taskId : taskIdSet) {
        	if (taskId == null) {
        	    allSuccessful = false;
        	    break;
        	}

            idList.add(taskId);
            taskList.add(taskData.getTaskInfo(taskId));
        	boolean isSuccessful = taskData.remove(taskId);
        	
        	if (!isSuccessful){
                allSuccessful = false;
                break;
        	}
        }
        
        if (allSuccessful) {
            TaskId[] taskIds = new TaskId[idList.size()];
            TaskInfo[] tasks = new TaskInfo[taskList.size()];
            
            idList.toArray(taskIds);
            taskList.toArray(tasks);
            return new DeleteResult(taskIds, tasks);
        } else {
            taskData.reverseLastChange();
            return new SimpleResult(Result.Type.DELETE_FAILURE);
        }
    }
       
}
