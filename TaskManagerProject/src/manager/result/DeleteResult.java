package manager.result;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class DeleteResult implements Result{
	
	private Type type;
	private TaskId taskId;
	private TaskInfo taskInfo;
	
	public DeleteResult(Type type, TaskId taskId, TaskInfo taskInfo){
	    assert type == Type.DELETE_SUCCESS;
		this.type = type;
		this.taskId = taskId;
		this.taskInfo = taskInfo;
	}

	@Override
	public Type getType() {
		return type;
	}
	
	public TaskId getTaskId(){
		return taskId;
	}

	public TaskInfo getTaskInfo(){
		return taskInfo;
	}
	
}
