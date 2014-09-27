package main.response;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class AddSuccessResponse implements Response{
	
	private Type type;
	private TaskInfo taskInfo;
	private TaskId taskId;
	
	public AddSuccessResponse(TaskInfo taskInfo, TaskId taskId){
		this.type = Type.ADD_SUCCESSFUL;
		this.taskInfo = taskInfo;
		this.taskId = taskId;
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
}
