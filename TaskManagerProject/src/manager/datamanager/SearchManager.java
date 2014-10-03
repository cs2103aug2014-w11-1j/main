package manager.datamanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

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
    
    private void sortTasks() {
        class InfoId {
            public TaskInfo taskInfo;
            public TaskId taskId;
            public InfoId(TaskInfo taskInfo, TaskId taskId) {
                this.taskInfo = taskInfo;
                this.taskId = taskId;
            }
        }
        int numberOfTasks = lastSearchedTasks.length;
        InfoId[] combinedList = new InfoId[numberOfTasks];
        for (int i = 0; i < numberOfTasks; i++) {
            combinedList[i] = new InfoId(lastSearchedTasks[i], 
                    lastSearchedTaskIds[i]);
        }
        Arrays.sort(combinedList, new Comparator<InfoId>() {
            public int compare(InfoId task1, InfoId task2) {
                if (task1.taskInfo.endDate.compareTo(task2.taskInfo.endDate) < 0) {
                    return -1;
                } else if (task1.taskInfo.endDate.compareTo(task2.taskInfo.endDate) > 0) {
                    return 1;
                } else {
                    return task1.taskInfo.endTime.compareTo(task2.taskInfo.endTime);
                }
            }
        });
        for (int i = 0; i < numberOfTasks; i++) {
            lastSearchedTasks[i] = combinedList[i].taskInfo;
            lastSearchedTaskIds[i] = combinedList[i].taskId;
        }
        
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
        sortTasks();
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