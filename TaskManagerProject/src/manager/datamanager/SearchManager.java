package manager.datamanager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.message.Message.Type;
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
    TaskInfoId[] lastSearchedTasks;
    String[] lastSearchedSuggestions;
    //Filter[] lastSearchFilters;

    class TaskInfoId {
        public TaskInfo taskInfo;
        public TaskId taskId;
        public TaskInfoId(TaskInfo taskInfo, TaskId taskId) {
            this.taskInfo = taskInfo;
            this.taskId = taskId;
        }
    }
    
    public SearchManager(TaskData taskData) {
        super(taskData);
        suggestionFinder = new SuggestionFinder(taskData);
    }

    private void sortTasks(List<TaskInfoId> tasks) {
        tasks.sort(new Comparator<TaskInfoId>() {
            @Override
            public int compare(TaskInfoId task1, TaskInfoId task2) {
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
    
    TaskInfoId[] split(TaskInfoId task) {
        List<TaskInfoId> result = new ArrayList<TaskInfoId>();
        if (task.taskInfo.getStartTime() == null) {
            result.add(task);
        } else {
            LocalDate currentDate = task.taskInfo.getStartDate();
            LocalTime currentTime = task.taskInfo.getStartTime();
            while (!currentDate.equals(task.taskInfo.getEndDate())) {
                TaskInfo taskInfo = new TaskInfo(task.taskInfo);
                TaskId taskId = task.taskId;
                taskInfo.startTime = currentTime;
                taskInfo.startDate = currentDate;
                taskInfo.endTime = LocalTime.MIDNIGHT;
                taskInfo.endDate = currentDate.plusDays(1);
                TaskInfoId taskInfoId = new TaskInfoId(taskInfo, taskId);
                result.add(taskInfoId);
                currentTime = LocalTime.parse("00:00");
                currentDate = currentDate.plusDays(1);
            }
            
            if (!currentTime.equals(task.taskInfo.getEndTime())) {
                TaskInfo taskInfo = new TaskInfo(task.taskInfo);
                TaskId taskId = task.taskId;
                taskInfo.startTime = currentTime;
                taskInfo.startDate = currentDate;
                taskInfo.endTime = task.taskInfo.endTime;
                taskInfo.endDate = task.taskInfo.endDate;
                TaskInfoId taskInfoId = new TaskInfoId(taskInfo, taskId);
                result.add(taskInfoId);
            }
        }
        
        TaskInfoId[] resultArray = new TaskInfoId[result.size()];
        result.toArray(resultArray);
        return resultArray;
    }
    
    TaskInfoId[] getTaskInfoIdArray(Set<TaskId> taskIds) {
        List<TaskInfoId> result = new ArrayList<TaskInfoId>();
        for (TaskId taskId : taskIds) {
            TaskInfo taskInfo = taskData.getTaskInfo(taskId);
            TaskInfoId taskInfoId = new TaskInfoId(taskInfo, taskId);
            Collections.addAll(result, split(taskInfoId));
        }

        sortTasks(result);
        TaskInfoId[] resultArray = new TaskInfoId[result.size()];
        result.toArray(resultArray);
        return resultArray;
    }
    
    private void updateSearchedTasks(Set<TaskId> taskIds) {
        lastSearchedTasks = getTaskInfoIdArray(taskIds);
    }
    
    private SearchResult searchWithSuggestion(Filter[] filters) {
        Set<TaskId> taskIds = findMatchingTasks(filters);
        
        updateSearchedTasks(taskIds);
        SearchResult result = new SearchResult(getInfoArray(lastSearchedTasks), 
                getIdArray(lastSearchedTasks), filters);
        
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
    
    /*public SearchResult searchWithoutUpdate(Filter[] filters) {
        Set <TaskId> taskIds = findMatchingTasks(filters);
        TaskInfoId[] infoIds = getTaskInfoIdArray(taskIds);
        
        SearchResult result = new SearchResult(Result.Type.SEARCH_SUCCESS,
                getInfoArray(infoIds),
                getIdArray(infoIds),
                filters);
        
        return result;
    }*/
    
    private SearchResult searchAndUpdate(Filter[] filters) {
        Set<TaskId> taskIds = findMatchingTasks(filters);
        updateSearchedTasks(taskIds);
        
        SearchResult result = new SearchResult(getInfoArray(lastSearchedTasks), 
                getIdArray(lastSearchedTasks), filters);
        
        lastSearchedSuggestions = null;
        return result;
    }
    
    private TaskInfo[] getInfoArray(TaskInfoId[] tasks) {
        TaskInfo[] infoArray = new TaskInfo[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            infoArray[i] = tasks[i].taskInfo;
        }
        return infoArray;
    }
    
    private TaskId[] getIdArray(TaskInfoId[] tasks) {
        TaskId[] idArray = new TaskId[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            idArray[i] = tasks[i].taskId;
        }
        return idArray;
    }
    
    public Result searchTasks(Filter[] filters) {
        assert filters != null : "filters can't be null";
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
    
    public SearchResult getLastSearchResult() {
        SearchResult result = new SearchResult(getInfoArray(lastSearchedTasks),
                getIdArray(lastSearchedTasks), null);

        return result;
    }
    
    public Result details(TaskId taskId) {
        return new DetailsResult(taskData.getTaskInfo(taskId), taskId);
    }

    public TaskId getAbsoluteIndex(int relativeIndex) {
        if (relativeIndex > lastSearchedTasks.length) {
            throw new IndexOutOfBoundsException();
        }
        return lastSearchedTasks[relativeIndex - 1].taskId;
    }

    public TaskInfo getTaskInfo(int relativeIndex) {
        if (relativeIndex >= lastSearchedTasks.length) {
            throw new IndexOutOfBoundsException();
        }
        return lastSearchedTasks[relativeIndex - 1].taskInfo;
    }

    public TaskInfo getTaskInfo(TaskId taskId) {
        return taskData.getTaskInfo(taskId);
    }
}