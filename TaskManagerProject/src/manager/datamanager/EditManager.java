package manager.datamanager;

import java.lang.reflect.Array;
import java.util.Arrays;

import javax.activation.FileDataSource;
import javax.xml.ws.Response;

import javafx.collections.SetChangeListener;
import io.FileInputOutput;
import main.message.EditSuccessfulMessage;
import manager.result.EditResult;
import manager.result.Result;
import manager.result.SimpleResult;
import data.TaskData;
import data.TaskId;
import data.taskinfo.Tag;
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
    	
    	EditSuccessfulMessage.Field[] fields = setChangedFields(taskInfo);
    	
    	return new EditResult(Result.Type.EDIT_SUCCESS,editedTask, taskId, fields);
    	
    }
    
    public Result editTaskwithTag(Tag tag, int operation, TaskId taskId){
    	boolean isTagSuccess;
    	if (operation == 1) {  //add
    		isTagSuccess = taskData.addTag(taskId, tag); 
    		if (!isTagSuccess){
    			return new SimpleResult(Result.Type.TAG_ADD_FAILURE);
    		}else{
    			return new EditResult(Result.Type.TAG_ADD_SUCCESS,taskData.getTaskInfo(taskId),
    					taskId, EditSuccessfulMessage.Field.TAGS_ADD);
    		}
    	} else  { //delete
    		isTagSuccess = taskData.removeTag(taskId, tag);
    		if (!isTagSuccess){
    			return new SimpleResult(Result.Type.TAG_DELETE_FAILURE);
    		}else{
    			return new EditResult(Result.Type.TAG_DELETE_SUCCESS,taskData.getTaskInfo(taskId),
    					taskId, EditSuccessfulMessage.Field.TAGS_DELETE);    		}
    	}
    	
    }
    
    
    /**
     * This method is to modify origin task with some changes specified
     * in modifTask, and return the modified task
     * @param originTask task to modify
     * @param modifTask changes to be modified
     * @return modified TaskInfo
     */
    public TaskInfo mergeTasks(TaskInfo originTask, TaskInfo modifTask ){
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
    
   
    public EditSuccessfulMessage.Field[] setChangedFields(TaskInfo taskInfo){
    	
    	EditSuccessfulMessage.Field[] fields = new EditSuccessfulMessage.Field[10];
    	int index = 0;
    	if (taskInfo.name != null){
    		fields[index] = EditSuccessfulMessage.Field.NAME;
    		index ++;
    	}
    	if (taskInfo.priority != null){
    		fields[index] = EditSuccessfulMessage.Field.PRIORITY;
    		index ++;
    	}
    	if (taskInfo.status != null){
    		fields[index] = EditSuccessfulMessage.Field.STATUS;
    		index ++;
    	}
    	if (taskInfo.details != null){
    		fields[index] = EditSuccessfulMessage.Field.DETAILS;
    		index ++;
    	}
    	if ((taskInfo.duration != null) || (taskInfo.endTime != null) ||
    			(taskInfo.endDate != null)){
    		fields[index] = EditSuccessfulMessage.Field.TIME;
    		index ++;
    	}
    	
    	return fields;
    }
}
