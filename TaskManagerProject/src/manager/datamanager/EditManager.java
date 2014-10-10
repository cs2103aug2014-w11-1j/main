package manager.datamanager;

import java.util.ArrayList;

import main.message.EditSuccessfulMessage;
import manager.result.EditResult;
import manager.result.Result;
import manager.result.SimpleResult;
import manager.result.StartEditModeResult;
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
    
    private TaskId editingTask;

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
    
    public TaskId getEditingTask() {
        return editingTask;
    }
    
    public Result startEditMode(TaskId taskId) {
        editingTask = taskId;
        return new StartEditModeResult(taskId);
    }
    
    public Result endEditMode(TaskId taskId) {
        editingTask = null;
        return new SimpleResult(Result.Type.EDIT_MODE_END);
    }
    
    public Result addTaskTag(Tag tag, TaskId taskId){
    	Boolean isTagSuccess = taskData.addTag(taskId, tag); 
		if (!isTagSuccess){
			return new SimpleResult(Result.Type.TAG_ADD_FAILURE);
		}else{
			return new EditResult(Result.Type.TAG_ADD_SUCCESS,taskData.getTaskInfo(taskId),
					taskId, EditSuccessfulMessage.Field.TAGS_ADD);
		}
    }
    
    public Result deleteTaskTag(Tag tag, TaskId taskId){
    	Boolean isTagSuccess = taskData.removeTag(taskId, tag);
    	if (!isTagSuccess){
    		return new SimpleResult(Result.Type.TAG_DELETE_FAILURE);
    	}else{
    		return new EditResult(Result.Type.TAG_DELETE_SUCCESS,taskData.getTaskInfo(taskId),
    				taskId, EditSuccessfulMessage.Field.TAGS_DELETE);  
    	}
    }
    
//    public Result editTaskwithTag(Tag tag, int operation, TaskId taskId){
//    	boolean isTagSuccess;
//    	if (operation == 1) {  //add
//    		isTagSuccess = taskData.addTag(taskId, tag); 
//    		if (!isTagSuccess){
//    			return new SimpleResult(Result.Type.TAG_ADD_FAILURE);
//    		}else{
//    			return new EditResult(Result.Type.TAG_ADD_SUCCESS,taskData.getTaskInfo(taskId),
//    					taskId, EditSuccessfulMessage.Field.TAGS_ADD);
//    		}
//    	} else  { //delete
//    		isTagSuccess = taskData.removeTag(taskId, tag);
//    		if (!isTagSuccess){
//    			return new SimpleResult(Result.Type.TAG_DELETE_FAILURE);
//    		}else{
//    			return new EditResult(Result.Type.TAG_DELETE_SUCCESS,taskData.getTaskInfo(taskId),
//    					taskId, EditSuccessfulMessage.Field.TAGS_DELETE);    		
//    			}
//    	}
//    	
//    }
    
    
    /**
     * This method is to modify origin task with some changes specified
     * in modifTask, and return the modified task
     * @param originTask task to modify
     * @param modifTask changes to be modified
     * @return modified TaskInfo
     */
    private TaskInfo mergeTasks(TaskInfo originTask, TaskInfo modifTask ){
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
    	if ((modifTask.priority != null)){
    		mergedTask.priority = modifTask.priority;
    	}
    	if ((modifTask.status != null)){
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
    
   
    private EditSuccessfulMessage.Field[] setChangedFields(TaskInfo taskInfo){
    	
        ArrayList<EditSuccessfulMessage.Field> fields = 
                new ArrayList<EditSuccessfulMessage.Field>();
    	int index = 0;
    	if (taskInfo.name != null){
    	    fields.add(EditSuccessfulMessage.Field.NAME);
    	}
    	if (taskInfo.priority != null){
    		fields.add(EditSuccessfulMessage.Field.PRIORITY);
    	}
    	if (taskInfo.status != null){
    		fields.add(EditSuccessfulMessage.Field.STATUS);
    	}
    	if (taskInfo.details != null){
    		fields.add(EditSuccessfulMessage.Field.DETAILS);
    	}
    	if ((taskInfo.duration != null) || (taskInfo.endTime != null) ||
    			(taskInfo.endDate != null)){
    		fields.add(EditSuccessfulMessage.Field.TIME);
    	}
    	
    	EditSuccessfulMessage.Field[] fieldsArray = 
    	        new EditSuccessfulMessage.Field[fields.size()];
    	for (int i = 0; i < fields.size(); i++) {
    	    fieldsArray[i] = fields.get(i);
    	}
    	
    	return fieldsArray;
    }
}
