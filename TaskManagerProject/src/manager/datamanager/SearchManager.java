package manager.datamanager;

import io.FileInputOutput;
import manager.datamanager.searchfilter.Filter;
import manager.result.Result;
import data.TaskData;
import data.TaskId;

public class SearchManager extends AbstractManager {

    public SearchManager(TaskData taskData) {
        super(taskData);
    }

    public Result searchTasks(Filter[] filters) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
    
    public TaskId getAbsoluteIndex(int relativeIndex) {
        // THIS IS A PLACEHOLDER! returns the first task in taskData.
        return taskData.getFirst();
    }
}