package main.response;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class EditSuccessResponse implements Response{

	private Type type;
	private TaskInfo taskInfo;
	private TaskId taskId;
	private boolean isInEditMode;
	
	public EditSuccessResponse(TaskInfo taskInfo, TaskId taskId, boolean isInEditMode){
		this.type = Type.EDIT_SUCCESS;
		this.taskInfo = taskInfo;
		this.taskId = taskId;
		this.isInEditMode = isInEditMode;
	}
	
	public Type getType(){
		return type;
	}
	
	public TaskInfo getTaskInfo(){
		return taskInfo;
	}
	
	public TaskId getTaskId(){
		return taskId;
	}
	
	public boolean checkEditMode(){
		return isInEditMode;
	}
}
