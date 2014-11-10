package manager.datamanager;

import java.util.ArrayList;
import java.util.List;

import main.command.EditCommand;
import main.command.TaskIdSet;
import main.message.EditSuccessfulMessage;
import main.message.EditSuccessfulMessage.Field;
import manager.result.EditResult;
import manager.result.Result;
import manager.result.SimpleResult;
import manager.result.StartEditModeResult;
import data.TaskData;
import data.TaskId;
import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

//@author A0119432L
/**
 * This is a edit manager that enables editing a certain TaskInfo with specific
 * taskId in the taskData.
 */
public class EditManager extends AbstractManager {

    private TaskIdSet editingTasks;

    public EditManager(TaskData taskData) {
        super(taskData);
    }

    /**
     * enable batch edit functionality
     * @param taskInfo taskInfo of editing content
     * @param taskIdSet task Ids that are to change
     * @return result of changing task
     */
    public Result editTask(TaskInfo taskInfo, TaskIdSet taskIdSet) {

        boolean allSuccessful = true;
        List<TaskId> taskIdList = new ArrayList<TaskId>();
        List<TaskInfo> taskList = new ArrayList<TaskInfo>();
        
        TaskInfo editedTask = null;
        EditSuccessfulMessage.Field[] fields = setChangedFields(taskInfo);

        for (TaskId taskId : taskIdSet) {
            if (taskId == null){
                allSuccessful = false;
                break;
            }

            TaskInfo originTask = taskData.getTaskInfo(taskId);
            if (originTask == null){
                allSuccessful = false;
                break;
            }

            editedTask = mergeTasks(originTask, taskInfo);
            boolean isSuccessful = taskData.setTaskInfo(taskId, editedTask);

            taskIdList.add(taskId);
            taskList.add(taskData.getTaskInfo(taskId));
            
            if (!isSuccessful){
                allSuccessful = false;
                break;
            }
        }

        if (allSuccessful) {
            TaskId[] taskIds = new TaskId[taskIdList.size()];
            TaskInfo[] tasks = new TaskInfo[taskList.size()];
            
            taskIdList.toArray(taskIds);
            taskList.toArray(tasks);
            
            return new EditResult(Result.Type.EDIT_SUCCESS, tasks, taskIds,
                    fields);
        } else {
            taskData.reverseLastChange();
            return new SimpleResult(Result.Type.EDIT_FAILURE);
        }
    }

    public TaskIdSet getEditingTasks() {
        return editingTasks;
    }
    
    /**
     * To clear certain info from a given set of task Ids
     * @param taskIdSet set of task Ids that are to change
     * @param infoToClear info to clear
     * @return
     */
    public Result clearInfo(TaskIdSet taskIdSet, EditCommand.Info infoToClear) {
        boolean allSuccessful = true;
        List<TaskId> taskIdList = new ArrayList<TaskId>();
        List<TaskInfo> taskList = new ArrayList<TaskInfo>();
        Field field = null;
        
        for (TaskId taskId : taskIdSet) {
            switch (infoToClear) {
                case TIME :
                    if (!tryClearTime(taskId)) {
                        allSuccessful = false;   
                    }
                    field = Field.TIME;
                    break;
                case DATE :
                case DATETIME :
                    if (!tryClearDate(taskId)) {
                        allSuccessful = false;
                    }
                    field = Field.TIME;
                    break;
                case DESCRIPTION :
                    if (!taskData.setTaskDetails(taskId, null)) {
                        allSuccessful = false;
                    }
                    field = Field.DETAILS;
                    break;
                case PRIORITY :
                    if (!taskData.setTaskPriority(taskId,
                            Priority.defaultPriority())) {
                        allSuccessful = false;
                    }
                    field = Field.PRIORITY;
                    break;
                case STATUS :
                    if (!taskData.setTaskStatus(taskId,
                            Status.defaultStatus())) {
                        allSuccessful = false;
                    }
                    field = Field.STATUS;
                    break;
                case TAGS :
                    if (!taskData.clearTags(taskId)) {
                        allSuccessful = false;
                    }
                    field = Field.TAGS_DELETE;
                    break;
                default :
                    allSuccessful = false;
                    break;
            }

            taskIdList.add(taskId);
            taskList.add(taskData.getTaskInfo(taskId));
            if (!allSuccessful) {
                break;
            }
        }
        

        if (allSuccessful) {
            TaskId[] taskIds = new TaskId[taskIdList.size()];
            TaskInfo[] tasks = new TaskInfo[taskList.size()];
            
            taskIdList.toArray(taskIds);
            taskList.toArray(tasks);
            
            return new EditResult(Result.Type.EDIT_SUCCESS, tasks,
                    taskIds, new Field[]{field});
        } else {
            taskData.reverseLastChange();
            return new SimpleResult(Result.Type.EDIT_FAILURE);
        }
    }

    private boolean tryClearTime(TaskId taskId) {
        if (!taskData.setTaskStartDate(taskId, null)) {
            return false;
        }
        if (!taskData.setTaskStartTime(taskId, null)) {
            return false;
        }
        if (!taskData.setTaskEndTime(taskId, null)) {
            return false;
        }
        
        return true;
    }

    private boolean tryClearDate(TaskId taskId) {
        if (!taskData.setTaskStartDate(taskId, null)) {
            return false;
        }
        if (!taskData.setTaskStartTime(taskId, null)) {
            return false;
        }
        if (!taskData.setTaskEndTime(taskId, null)) {
            return false;
        }
        if (!taskData.setTaskDate(taskId, null)) {
            return false;
        }
        
        return true;
    }

    /**
     * THis is to start editing mode and note the editing id set
     * @param taskIdSet
     * @return
     */
    public Result startEditMode(TaskIdSet taskIdSet) {
        editingTasks = taskIdSet;
        return new StartEditModeResult(taskIdSet);
    }

    public Result endEditMode() {
        editingTasks = null;
        return new SimpleResult(Result.Type.EDIT_MODE_END);
    }

    /**
     * This is to add tags to multiple tasks
     * @param tags tags list to add
     * @param taskIdSet set of tasks to add tags
     * @return result of adding tags
     */
    public Result addTaskTags(Tag[] tags, TaskIdSet taskIdSet){
        boolean allSuccessful = true;
        
        List<TaskId> taskIdList = new ArrayList<TaskId>();
        List<TaskInfo> taskList = new ArrayList<TaskInfo>();

        for (TaskId taskId : taskIdSet) {
            if (taskId == null){
                allSuccessful = false;
                break;
            }

            boolean isSuccessful = taskData.taskExists(taskId);
            for (Tag tag : tags) {
                taskData.addTag(taskId, tag);
            }

            taskIdList.add(taskId);
            taskList.add(taskData.getTaskInfo(taskId));
            
            if (!isSuccessful) {
                allSuccessful = false;
                break;
            }
        }

        if (allSuccessful) {
            TaskId[] taskIds = new TaskId[taskIdList.size()];
            TaskInfo[] tasks = new TaskInfo[taskList.size()];
            
            taskIdList.toArray(taskIds);
            taskList.toArray(tasks);
            
            return new EditResult(Result.Type.TAG_ADD_SUCCESS, tasks, taskIds,
                     EditSuccessfulMessage.Field.TAGS_ADD);
        } else {
            taskData.reverseLastChange();
            return new SimpleResult(Result.Type.TAG_ADD_FAILURE);
        }
    }

    /**
     * This is to delete tags to multiple tasks
     * @param tags tags list to delete
     * @param taskIdSet set of tasks to delete tags
     * @return result of deleting tags
     */
    public Result deleteTaskTags(Tag[] tags, TaskIdSet taskIdSet){

        boolean allSuccessful = true;
        List<TaskId> taskIdList = new ArrayList<TaskId>();
        List<TaskInfo> taskList = new ArrayList<TaskInfo>();

        for (TaskId taskId : taskIdSet) {
            if (taskId == null) {
                allSuccessful = false;
                break;
            }

            boolean isSuccessful = taskData.taskExists(taskId);
            boolean isTagSuccess = false;
            for (Tag tag : tags) {
                Boolean  isDeleteSuccess = taskData.removeTag(taskId, tag);
                if (isDeleteSuccess){
                	isTagSuccess = true;
                }
            }

            taskIdList.add(taskId);
            taskList.add(taskData.getTaskInfo(taskId));
            
            if ((!isSuccessful) || (!isTagSuccess)) {
                allSuccessful = false;
                break;
            }
        }

        if (allSuccessful) {
            TaskId[] taskIds = new TaskId[taskIdList.size()];
            TaskInfo[] tasks = new TaskInfo[taskList.size()];
            
            taskIdList.toArray(taskIds);
            taskList.toArray(tasks);
            
            return new EditResult(Result.Type.TAG_DELETE_SUCCESS, tasks, taskIds, 
                    EditSuccessfulMessage.Field.TAGS_DELETE);
        } else {
            taskData.reverseLastChange();
            return new SimpleResult(Result.Type.TAG_DELETE_FAILURE);
        }
    }


    /**
     * This method is to modify origin task with some changes specified
     * in modifTask, and return the modified task
     * @param originTask task to modify
     * @param modifTask changes to be modified
     * @return modified TaskInfo
     */
    private TaskInfo mergeTasks(TaskInfo originTask, TaskInfo modifTask ){
    	TaskInfo mergedTask = new TaskInfo(originTask);
    	
    	mergedTask = mergeTasksTime(originTask, modifTask);
    	if (modifTask.name != null){
    		mergedTask.name = modifTask.name;
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


    
    /**
     * This is to merge the time between origin taskInfo and modifying taskInfo
     * @param originTask original taskInfo
     * @param modifTask modifying taskInfo
     * @return Modified task
     */
    private TaskInfo mergeTasksTime(TaskInfo originTask, TaskInfo modifTask){
    	TaskInfo mergedTask = new TaskInfo(originTask);
    	if ((modifTask.startDate == null) && (modifTask.startTime == null)
    		&& (modifTask.endDate != null) && (modifTask.endTime == null)){
    		mergedTask.startTime = null;
    		mergedTask.startDate = null;
    		mergedTask.endDate = modifTask.endDate;
    	}
    	if ((modifTask.startDate == null) && (modifTask.startTime == null)
        		&& (modifTask.endDate == null) && (modifTask.endTime != null)){
    		mergedTask.startTime = null;
    		mergedTask.startDate = null;
    		mergedTask.endTime = modifTask.endTime;
    	}
    	if ((modifTask.startDate == null) && (modifTask.startTime == null)
        		&& (modifTask.endDate != null) && (modifTask.endTime != null)){
    		mergedTask.startTime = null;
    		mergedTask.startDate = null;
    		mergedTask.endDate = modifTask.endDate;
    		mergedTask.endTime = modifTask.endTime;
    	}
    	if ((modifTask.startDate != null) && (modifTask.startTime == null)
        		&& (modifTask.endDate != null) && (modifTask.endTime == null)){
    		mergedTask.startDate = modifTask.startDate;
    		mergedTask.endDate = modifTask.endDate;
    	}
    	if ((modifTask.startDate == null) && (modifTask.startTime != null)
        		&& (modifTask.endDate == null) && (modifTask.endTime != null)){
    		mergedTask.startTime = modifTask.startTime;
    		mergedTask.endTime = modifTask.endTime;
    		if (mergedTask.startDate == null){
    			mergedTask.startDate = mergedTask.endDate;
    		}
    	}
    	if ((modifTask.startDate != null) && (modifTask.startTime != null)
        		&& (modifTask.endDate != null) && (modifTask.endTime != null)){
    		mergedTask.startTime = modifTask.startTime;
    		mergedTask.startDate = modifTask.startDate;
    		mergedTask.endDate = modifTask.endDate;
    		mergedTask.endTime = modifTask.endTime;
    	}
    	return mergedTask;
    }
    
    private EditSuccessfulMessage.Field[] setChangedFields(TaskInfo taskInfo){

        ArrayList<EditSuccessfulMessage.Field> fields =
                new ArrayList<EditSuccessfulMessage.Field>();
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
    	if ((taskInfo.startTime != null) || (taskInfo.startDate != null) ||
    	        (taskInfo.endTime != null) || (taskInfo.endDate != null)) {
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
