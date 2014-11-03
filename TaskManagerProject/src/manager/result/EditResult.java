package manager.result;

import main.message.EditSuccessfulMessage;
import data.TaskId;
import data.taskinfo.TaskInfo;

//@author A0119432L
public class EditResult implements Result {
	
	private Type type;
	private TaskInfo[] tasks;
	private TaskId[] taskIds;
	private EditSuccessfulMessage.Field[] changedFields;

	public EditResult (Type type, TaskInfo[] tasks, TaskId[] taskIds, EditSuccessfulMessage.Field[] fields){
	    assert type == Type.EDIT_SUCCESS;
		this.type = type;
		this.tasks = tasks;
		this.taskIds = taskIds;
		this.changedFields = fields;
	}
	
	public EditResult (Type type, TaskInfo[] tasks, TaskId[] taskIds, EditSuccessfulMessage.Field field){
		this.type = type;
		this.tasks = tasks;
		this.taskIds = taskIds;
		this.changedFields = new EditSuccessfulMessage.Field[1];
		changedFields[0] = field;
	}

	@Override
	public Type getType() {
		return type;
	}
	
	public TaskInfo[] getTasks(){
		return tasks;
	}
	
	public TaskId[] getTaskIds(){
		return taskIds;
	}

	public EditSuccessfulMessage.Field[] getChangedFields(){
		return changedFields;
	}

}
