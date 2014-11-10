package manager.datamanager;

import manager.result.AddResult;
import manager.result.Result;
import manager.result.SimpleResult;
import data.TaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;


//@author A0119432L
/**
 * This is an add manager that enables creating a new task with given
 * taskInfo in the TaskData.
 */
public class AddManager extends AbstractManager {
	
	private TaskId id = null;
    
    public AddManager(TaskData taskData) {
        super(taskData);
    }

    public Result addTask(TaskInfo taskInfo) {
    	if (taskInfo == null){
    		return new SimpleResult(Result.Type.ADD_FAILURE);
    	}
    	taskInfo.assertValidDateTime();
    	
    	id = taskData.add(taskInfo);
    	
    	if (id == null){
    		return new SimpleResult(Result.Type.ADD_FAILURE);
    	}
    	
    	return new AddResult(taskInfo, id);
    	
    }
    
}
