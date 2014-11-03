package manager.result;

import data.TaskId;
import data.taskinfo.TaskInfo;

//@author A0119432L
public class DeleteResult implements Result{
	
	private TaskId[] taskIds;
	private TaskInfo[] tasks;
	
	public DeleteResult(TaskId[] taskIds, TaskInfo[] tasks){
		this.taskIds = taskIds;
		this.tasks = tasks;
	}

	@Override
	public Type getType() {
		return Type.DELETE_SUCCESS;
	}
	
	public TaskId[] getTaskIds(){
		return taskIds;
	}

	public TaskInfo[] getTasks(){
		return tasks;
	}
	
}
