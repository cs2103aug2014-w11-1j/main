package manager.datamanager;

import io.FileInputOutput;
import data.TaskData;
import manager.Result;
import manager.datamanager.searchfilter.Filter;

public class SearchManager extends AbstractManager {

    public SearchManager(FileInputOutput fileInputOutput, TaskData taskData) {
        super(fileInputOutput, taskData);
        // TODO Auto-generated constructor stub
    }

    public Result searchTasks(Filter[] filters) {
        throw new UnsupportedOperationException("Not Implemented Yet");
    }
}