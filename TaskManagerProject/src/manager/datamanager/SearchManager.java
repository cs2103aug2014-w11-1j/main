package manager.datamanager;

import java.util.ArrayList;

import com.sun.javafx.scene.control.skin.FXVK.Type;

import manager.datamanager.searchfilter.Filter;
import manager.result.Result;
import manager.result.SearchResult;
import data.TaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;

public class SearchManager extends AbstractManager {

    TaskId[] lastSearchedTaskIds;
    TaskInfo[] lastSearchedTasks;
    Filter[] lastSearchFilters;
    
    public SearchManager(TaskData taskData) {
        super(taskData);
    }
    
    private boolean matchFilter(TaskInfo taskInfo, Filter[] filters) {
        for (Filter filter : filters){
            if (!filter.filter(taskInfo)) {
                return false;
            }
        }
        return true;
    }
    
    private void updateLastSearched(Filter[] filters) {
        ArrayList<TaskId> taskIdList = new ArrayList<TaskId>();
        ArrayList<TaskInfo> taskList = new ArrayList<TaskInfo>();
        TaskId currentId = taskData.getFirst();
        if (taskData.taskExists(currentId)) {
            TaskId lastId = taskData.getLast();
            while (true) {
                TaskInfo task = taskData.getTaskInfo(currentId);
                if (matchFilter(task, filters)) {
                    taskList.add(task);
                    taskIdList.add(currentId);
                }
                if (lastId.equals(currentId)) {
                    break;
                }
                currentId = taskData.getNext(currentId);
            }
        }
        lastSearchedTasks = (TaskInfo[])taskList.toArray();
        lastSearchedTaskIds = (TaskId[])taskIdList.toArray();
    }

    public Result searchTasks(Filter[] filters) {
        lastSearchFilters = filters;
        updateLastSearched(filters);
        SearchResult result = new SearchResult(Result.Type.SEARCH_SUCCESS, 
                lastSearchedTasks, lastSearchedTaskIds);
        return result;
    }
    
    public Result redoLastSearch() {
        return searchTasks(lastSearchFilters);
    }
    
    public TaskId getAbsoluteIndex(int relativeIndex) {
        return lastSearchedTaskIds[relativeIndex - 1];
    }
    
    public void searchAgain(){
    	// search again after each successfully executed command
    }
}