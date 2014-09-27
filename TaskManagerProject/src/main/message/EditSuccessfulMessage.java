package main.message;

import data.taskinfo.TaskInfo;

public class EditSuccessfulMessage implements Message{
    
    public enum Field {
        NAME,
        TIME,
        PRIORITY,
        TAGS_ADD,
        TAGS_DELETE,
    }
    
    Field changedField;
    private TaskInfo task;
    
    public EditSuccessfulMessage(TaskInfo task, Field changedField) {
        this.task = task;
        this.changedField = changedField;
    }
    
    public Type getType() {
        return Type.EDIT_SUCCESSFUL;
    }
    
    public Field getChangedField() {
        return changedField;
    }
    
    public TaskInfo getTask() {
        return task;
    }
}
