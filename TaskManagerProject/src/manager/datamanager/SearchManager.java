package manager.datamanager;

import io.FileInputOutput;
import data.TaskData;
import manager.datamanager.searchfilter.Filter;
import manager.result.Result;

public class SearchManager extends AbstractManager {

    public SearchManager(FileInputOutput fileInputOutput, TaskData taskData) {
        super(fileInputOutput, taskData);
    }

    public Result searchTasks(Filter[] filters) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
}