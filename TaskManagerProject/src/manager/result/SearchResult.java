package manager.result;

import data.TaskId;
import data.taskinfo.TaskInfo;

public class SearchResult implements Result {

    private TaskInfo[] tasks;
    private TaskId[] taskIds;
    Type type;
    private String[] suggestions;
    
    public SearchResult(Type type, TaskInfo[] tasks, TaskId[] taskIds) {
        this.type = type;
        this.tasks = tasks;
        this.taskIds = taskIds;
    }
    
    public TaskId[] getTaskIds() {
        return taskIds;
    }
    
    public TaskInfo[] getTasks() {
        return tasks;
    }
    
    public void setSuggestion(String[] suggestions) {
        this.suggestions = suggestions;
    }
    
    @Override
    public Type getType() {
        return type;
    }

}
