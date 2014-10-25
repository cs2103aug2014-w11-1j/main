package manager.result;

import manager.datamanager.searchfilter.Filter;
import data.TaskId;
import data.taskinfo.TaskInfo;

public class SearchResult implements Result {

    private TaskInfo[] tasks;
    private TaskId[] taskIds;
    private Filter[] filtersUsed;
    private String[] suggestions;
    
    public SearchResult(TaskInfo[] tasks, TaskId[] taskIds,
            Filter[] filtersUsed) {
        this.tasks = tasks;
        this.taskIds = taskIds;
        this.filtersUsed = filtersUsed;
    }
    
    public TaskId[] getTaskIds() {
        assert tasks.length == taskIds.length;
        return taskIds;
    }
    
    public TaskInfo[] getTasks() {
        assert tasks.length == taskIds.length;
        return tasks;
    }
    
    public Filter[] getFilters() {
        return filtersUsed;
    }
    
    public void setSuggestion(String[] suggestions) {
        this.suggestions = suggestions;
    }
    
    public String[] getSuggestions() {
        return suggestions;
    }
    
    @Override
    public Type getType() {
        return Type.SEARCH_SUCCESS;
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
