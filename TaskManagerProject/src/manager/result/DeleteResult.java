package manager.result;

import data.TaskId;

public class DeleteResult implements Result{
	
	private Type type;
	private TaskId taskId;
	
	public DeleteResult(Type type, TaskId taskId){
		this.type = type;
		this.taskId = taskId;
	}

	@Override
	public Type getType() {
		return type;
	}
	
	public TaskId getTaskId(){
		return taskId;
	}

	
}
