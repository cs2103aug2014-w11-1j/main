package manager.datamanager;

import java.util.ArrayList;

import main.command.TaskIdSet;
import main.message.EditSuccessfulMessage;
import manager.result.DeleteResult;
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
    
    private TaskIdSet editingTasks;

    public EditManager(TaskData taskData) {
        super(taskData);
    }

    public Result editTask(TaskInfo taskInfo, TaskIdSet taskIdSet) {
    	
        boolean allSuccessful = true;
        TaskId returnTaskId = null;
        TaskInfo editedTask = null;
        EditSuccessfulMessage.Field[] fields = setChangedFields(taskInfo);
        
        for (TaskId taskId : taskIdSet) {
            if (taskId == null){
                allSuccessful = false;
                break;
            }

            TaskInfo originTask = taskData.getTaskInfo(taskId);
            if (originTask == null){
                return new SimpleResult(Result.Type.EDIT_FAILURE);
            }
            // PLACEHOLDER : Change later to a proper batch response.
            editedTask = mergeTasks(originTask, taskInfo);
            returnTaskId = taskId;
            boolean isSuccessful = taskData.setTaskInfo(taskId, editedTask);
            
            if (!isSuccessful){
                allSuccessful = false;
                break;
            }
        }
        
        if (allSuccessful) {
            return new EditResult(Result.Type.EDIT_SUCCESS,editedTask,
                    returnTaskId, fields);
        } else {
            taskData.reverseLastChange();
            return new SimpleResult(Result.Type.EDIT_FAILURE);
        }
    }
    
    public TaskIdSet getEditingTasks() {
        return editingTasks;
    }
    
    public Result startEditMode(TaskIdSet taskIdSet) {
        editingTasks = taskIdSet;
        return new StartEditModeResult(taskIdSet);
    }
    
    public Result endEditMode() {
        editingTasks = null;
        return new SimpleResult(Result.Type.EDIT_MODE_END);
    }
    
    public Result addTaskTag(Tag tag, TaskIdSet taskIdSet){
        boolean allSuccessful = true;
        TaskId returnTaskId = null;
        
        for (TaskId taskId : taskIdSet) {
            if (taskId == null){
                allSuccessful = false;
                break;
            }
            
            boolean isSuccessful = taskData.taskExists(taskId);
            boolean isTagSuccess = taskData.addTag(taskId, tag);
            
            if (!isSuccessful) {
                allSuccessful = false;
                break;
            }
        }
        
        if (allSuccessful) {
            return new EditResult(Result.Type.TAG_ADD_SUCCESS,taskData.getTaskInfo(returnTaskId),
                    returnTaskId, EditSuccessfulMessage.Field.TAGS_ADD);
        } else {
            taskData.reverseLastChange();
            return new SimpleResult(Result.Type.TAG_ADD_FAILURE);
        }
    }
    
    public Result deleteTaskTag(Tag tag, TaskIdSet taskIdSet){

        boolean allSuccessful = true;
        TaskId returnTaskId = null;
        
        for (TaskId taskId : taskIdSet) {
            if (taskId == null){
                allSuccessful = false;
                break;
            }
            
        	boolean isTagSuccess = taskData.removeTag(taskId, tag);
            boolean isSuccessful = taskData.taskExists(taskId);

            if (!isSuccessful) {
                allSuccessful = false;
                break;
            }
        }
    	
        if (allSuccessful) {
            return new EditResult(Result.Type.TAG_DELETE_SUCCESS,taskData.getTaskInfo(returnTaskId),
                    returnTaskId, EditSuccessfulMessage.Field.TAGS_DELETE);
        } else {
            taskData.reverseLastChange();
            return new SimpleResult(Result.Type.TAG_DELETE_FAILURE);
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
