package manager.datamanager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import manager.datamanager.searchfilter.Filter;
import manager.datamanager.searchfilter.SuggestionFilter;
import manager.datamanager.suggestion.SuggestionFinder;
import manager.result.DetailsResult;
import manager.result.Result;
import manager.result.SearchResult;
import taskline.debug.Taskline;
import data.TaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;

public class SearchManager extends AbstractManager {
    private static final Logger log = Logger.getLogger(Taskline.LOGGER_NAME);

    
    SuggestionFinder suggestionFinder;
    TaskId[] lastSearchedTaskIds;
    TaskInfo[] lastSearchedTasks;
    String[] lastSearchedSuggestions;
    Filter[] lastSearchFilters;

    public SearchManager(TaskData taskData) {
        super(taskData);
        lastSearchFilters = new Filter[0];
        suggestionFinder = new SuggestionFinder(taskData);
    }
    
    class InfoId {
        public TaskInfo taskInfo;
        public TaskId taskId;
        public InfoId(TaskInfo taskInfo, TaskId taskId) {
            this.taskInfo = taskInfo;
            this.taskId = taskId;
        }
    }

    private void sortTasks(TaskInfo[] tasks, TaskId[] taskIds) {
        int numberOfTasks = taskIds.length;
        InfoId[] combinedList = new InfoId[numberOfTasks];
        for (int i = 0; i < numberOfTasks; i++) {
            combinedList[i] = new InfoId(tasks[i],
                    taskIds[i]);
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
            tasks[i] = combinedList[i].taskInfo;
            taskIds[i] = combinedList[i].taskId;
        }
    }
    
    private Set<TaskId> applyFilter(Filter filter) {
        Set<TaskId> resultSet = new HashSet<TaskId>();
        
        TaskId currentId = taskData.getFirst();
        while (currentId.isValid()) {
            TaskInfo task = taskData.getTaskInfo(currentId);
            if (filter.filter(task)) {
                resultSet.add(currentId);
            }
            currentId = taskData.getNext(currentId);
        }
        
        return resultSet;
    }
    
    private Set<TaskId> findMatchingTasks(Filter[] filters) {
        Set<TaskId> currentMatch = getAllId();
        
        for (Filter filter : filters) {
            Set<TaskId> matching = applyFilter(filter);
            currentMatch.retainAll(matching);
        }
        
        return currentMatch;
    }
    
    private Set<TaskId> getAllId() {
        Set<TaskId> resultSet = new HashSet<TaskId>();
        
        TaskId currentId = taskData.getFirst();
        while (currentId.isValid()) {
            resultSet.add(currentId);
            currentId = taskData.getNext(currentId);
        }
        
        return resultSet;
    }
    
    void updateSearchedTasks(Collection<TaskId> taskIds) {
        lastSearchedTaskIds = new TaskId[taskIds.size()];
        lastSearchedTasks = new TaskInfo[taskIds.size()];
        
        taskIds.toArray(lastSearchedTaskIds);
        
        for (int i = 0; i < lastSearchedTaskIds.length; i++) {
            lastSearchedTasks[i] = 
                    taskData.getTaskInfo(lastSearchedTaskIds[i]);
        }
        sortTasks(lastSearchedTasks, lastSearchedTaskIds);
    }
    
    private SearchResult getResultFromIds(Collection<TaskId> taskIds) {
        TaskId idArray[] = new TaskId[taskIds.size()];
        TaskInfo taskArray[] = new TaskInfo[taskIds.size()];
        
        taskIds.toArray(idArray);
        
        for (int i = 0; i < idArray.length; i++) {
            taskArray[i] = taskData.getTaskInfo(idArray[i]);
        }
        
        SearchResult result = new SearchResult(Result.Type.SEARCH_SUCCESS, 
                taskArray, idArray);
        return result;
    }
    
    public SearchResult searchWithSuggestion(Filter[] filters) {
        Collection<TaskId> taskIds = findMatchingTasks(filters);
        
        updateSearchedTasks(taskIds);
        SearchResult result = getResultFromIds(taskIds);
        
        List<String> suggestions = new ArrayList<String>();
        for (int i = 0; i < filters.length; i++) {
            if (filters[i].getType() == Filter.Type.FILTER_SUGGESTION) {
                SuggestionFilter filter = (SuggestionFilter)filters[i];
                suggestions.add(filter.getTopSuggestion());
            }
        }
        
        String[] suggestionArray = new String[suggestions.size()];
        suggestions.toArray(suggestionArray);
        result.setSuggestion(suggestionArray);
        lastSearchedSuggestions = suggestionArray;
        
        return result;
    }
    
    private SearchResult searchAndUpdate(Filter[] filters) {
        Collection<TaskId> taskIds = findMatchingTasks(filters);
        updateSearchedTasks(taskIds);
        
        SearchResult result = new SearchResult(Result.Type.SEARCH_SUCCESS, 
                lastSearchedTasks, lastSearchedTaskIds);
        
        lastSearchedSuggestions = null;
        return result;
    }
    
    public Result searchTasks(Filter[] filters) {
        lastSearchFilters = filters;
        log.log(Level.FINER, "Conduct search: " + filters.length + " filters");
        
        SearchResult result = searchAndUpdate(filters);
        
        if (result.getTaskIds().length == 0) {
            Filter[] newFilters = 
                    suggestionFinder.generateSuggestionFilters(filters);
            if (newFilters == null) {
                return result;
            } else {
                return searchWithSuggestion(newFilters);
            }
        } else {
            return result;
        }
    }

    public Result redoLastSearch() {
        return searchTasks(lastSearchFilters);
    }
    
    public SearchResult getLastSearchResult() {
        SearchResult result = (SearchResult) redoLastSearch();
        return result;
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