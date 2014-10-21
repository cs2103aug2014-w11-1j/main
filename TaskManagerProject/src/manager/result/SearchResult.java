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
        assert tasks.length == taskIds.length;
        return taskIds;
    }
    
    public TaskInfo[] getTasks() {
        assert tasks.length == taskIds.length;
        return tasks;
    }
    
    public void setSuggestion(String[] suggestions) {
        this.suggestions = suggestions;
    }
    
    public String[] getSuggestions() {
        return suggestions;
    }
    
    @Override
    public Type getType() {
        return type;
    }

    /**
     * @return true iff there is only one result in the search.
     */
    public boolean onlyOneSearchResult() {
        assert tasks.length == taskIds.length;
        return taskIds.length == 1;
    }

    /**
     * Note: There must be exactly one search result.
     * @return The taskId of the only search result.
     */
    public TaskId getOnlySearchResult() {
        assert onlyOneSearchResult();
        return taskIds[0];
    }
}
