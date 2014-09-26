package main.response;

import data.TaskId;

public class DeleteSuccessResponse implements Response{
	
	private Type type;
	private TaskId taskId;
	
	public DeleteSuccessResponse (TaskId taskId){
		this.type = Type.DELETE_SUCCESSFUL;
		this.taskId = taskId;
	}
	
	public Type getType(){
		return type;
	}

}
