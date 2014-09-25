package manager.datamanager;

import io.FileInputOutput;
import manager.result.EditResult;
import manager.result.Result;
import manager.result.SimpleResult;
import data.TaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;

/**
 * This is a edit manager that enables editing a certain TaskInfo with specific 
 * taskId in the taskData.
 * @author BRUCE
 *
 */
public class EditManager extends AbstractManager {

    public EditManager(TaskData taskData) {
        super(taskData);
    }

    public Result editTask(TaskInfo taskInfo, TaskId taskId) {
    	
    	if (taskId == null){
    		return new SimpleResult(Result.Type.EDIT_FAILURE);
    	}
    	
    	TaskInfo originTask = taskData.getTaskInfo(taskId);
    	if (originTask == null){
    		return new SimpleResult(Result.Type.EDIT_FAILURE);
    	}
    	TaskInfo editedTask = mergeTasks(originTask, taskInfo);
    	taskData.setTaskInfo(taskId, editedTask);
    	
    	return new EditResult(Result.Type.EDIT_SUCCESS,editedTask, taskId);
    	
    }
    
    /**
     * This method is to modify origin task with some changes specified
     * in modifTask, and return the modified task
     * @param originTask task to modify
     * @param modifTask changes to be modified
     * @return modified TaskInfo
     */
    public TaskInfo mergeTasks(TaskInfo originTask, TaskInfo modifTask){
    	TaskInfo mergedTask = new TaskInfo(originTask);
    	if (modifTask.name != null){
    		mergedTask.name = modifTask.name;
    	}
    	if (modifTask.duration != null){
    		mergedTask.duration = modifTask.duration;
    	}
    	if (modifTask.endTime != null){
    		mergedTask.endTime = modifTask.endTime;
    	}
    	if (modifTask.endDate != null){
    		mergedTask.endDate = modifTask.endDate;
    	}
    	if (modifTask.details != null){
    		mergedTask.details = modifTask.details;
    	}
    	if (modifTask.priority != null){
    		mergedTask.priority = modifTask.priority;
    	}
    	if (modifTask.status != null){
    		mergedTask.status = modifTask.status;
    	}
    	if (modifTask.numberOfTimes != 0){
    		mergedTask.numberOfTimes = modifTask.numberOfTimes;
    	}
    	if (modifTask.repeatInterval != null){
    		mergedTask.repeatInterval = modifTask.repeatInterval;
    	}
    	
    	return mergedTask;
    }
}
