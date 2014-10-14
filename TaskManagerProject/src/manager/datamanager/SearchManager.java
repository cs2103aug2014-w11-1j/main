package manager.datamanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

import manager.datamanager.searchfilter.Filter;
import manager.result.DetailsResult;
import manager.result.Result;
import manager.result.SearchResult;
import taskline.debug.Taskline;
import data.TaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;

public class SearchManager extends AbstractManager {
    private static final Logger log = Logger.getLogger(Taskline.LOGGER_NAME);

    TaskId[] lastSearchedTaskIds;
    TaskInfo[] lastSearchedTasks;
    Filter[] lastSearchFilters;

    public SearchManager(TaskData taskData) {
        super(taskData);
        lastSearchFilters = new Filter[0];
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
            @Override
            public int compare(InfoId task1, InfoId task2) {
                if (task1.taskInfo.endDate == null) {
                    if (task2.taskInfo.endDate == null) {
                        return 0;
                    }
                    else {
                        return 1;
                    }
                } else if (task2.taskInfo.endDate == null) {
                    return -1;
                }
                
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
        while (currentId.isValid()) {
            TaskInfo task = taskData.getTaskInfo(currentId);
            if (matchFilter(task, filters)) {
                taskList.add(task);
                taskIdList.add(currentId);
            }
            currentId = taskData.getNext(currentId);
        }
        
        lastSearchedTasks = taskList.toArray(new TaskInfo[taskList.size()]);
        lastSearchedTaskIds = taskIdList.toArray(new TaskId[taskIdList.size()]);
        sortTasks();
    }

    public Result searchTasks(Filter[] filters) {
        log.log(Level.FINER, "Conduct search: " + filters.length + " filters");
        
        lastSearchFilters = filters;
        updateLastSearched(filters);
        SearchResult result = new SearchResult(Result.Type.SEARCH_SUCCESS,
                lastSearchedTasks, lastSearchedTaskIds);
        return result;
    }

    public Result redoLastSearch() {
        return searchTasks(lastSearchFilters);
    }
    
    public SearchResult getLastSearchResult() {
        return new SearchResult(Result.Type.SEARCH_SUCCESS,
                lastSearchedTasks, lastSearchedTaskIds);
    }
    
    public Result details(TaskId taskId) {
        return new DetailsResult(taskData.getTaskInfo(taskId), taskId);
    }

    public TaskId getAbsoluteIndex(int relativeIndex) {
        if (relativeIndex > lastSearchedTaskIds.length) {
            throw new IndexOutOfBoundsException();
        }
        return lastSearchedTaskIds[relativeIndex - 1];
    }

    public TaskInfo getTaskInfo(int relativeIndex) {
        if (relativeIndex >= lastSearchedTaskIds.length) {
            throw new IndexOutOfBoundsException();
        }
        return lastSearchedTasks[relativeIndex - 1];
    }

    public TaskInfo getTaskInfo(TaskId taskId) {
        return taskData.getTaskInfo(taskId);
    }
}