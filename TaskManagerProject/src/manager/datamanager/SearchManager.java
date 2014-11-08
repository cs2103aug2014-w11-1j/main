package manager.datamanager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.command.TaskIdSet;
import manager.datamanager.searchfilter.Filter;
import manager.datamanager.searchfilter.SuggestionFilter;
import manager.datamanager.suggestion.SuggestionFinder;
import manager.result.DetailsResult;
import manager.result.Result;
import manager.result.SearchResult;
import taskline.TasklineLogger;
import data.TaskData;
import data.TaskId;
import data.taskinfo.TaskInfo;

//@author A0113011L
public class SearchManager extends AbstractManager {
    private static final Logger log = TasklineLogger.getLogger();

    SuggestionFinder suggestionFinder;
    TaskInfoId[] lastSearchedTasks;
    String[] lastSearchedSuggestions;

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

    private void sortTasks(TaskInfoId[] tasks) {
        Arrays.sort(tasks, new Comparator<TaskInfoId>() {
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
                    if (task1.taskInfo.endTime == null) {
                        return 1;
                    } else if (task2.taskInfo.endTime == null) {
                        return -1;
                    } else {
                        return task1.taskInfo.endTime.compareTo(task2.taskInfo.endTime);
                    }
                }
            }
        });
    }
    
    private Set<TaskId> applyFilter(Filter filter) {
        Set<TaskId> resultSet = new HashSet<TaskId>();
        
        TaskId currentId = taskData.getFirst();
        while (currentId.isValid()) {
            TaskInfo task = taskData.getTaskInfo(currentId);
            if (filter.isMatching(task)) {
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
            Collections.addAll(result, taskInfoId);
        }

        TaskInfoId[] resultArray = new TaskInfoId[result.size()];
        result.toArray(resultArray);
        return resultArray;
    }
    
    private void updateSearchedTasks(Set<TaskId> taskIds) {
        lastSearchedTasks = getTaskInfoIdArray(taskIds);
    }
    
    private TaskInfoId[] splitAll(TaskInfoId[] oldArray) {
        List<TaskInfoId> splittedList = new ArrayList<TaskInfoId>();
        
        for (TaskInfoId task : oldArray) {
            Collections.addAll(splittedList, split(task));
        }
        
        TaskInfoId[] newArray = new TaskInfoId[splittedList.size()];
        
        splittedList.toArray(newArray);
        return newArray;
    }
    
    private TaskInfoId[] refilter(Filter[] filters, TaskInfoId[] taskInfoIds) {
        List<TaskInfoId> filteredList = new ArrayList<TaskInfoId>();
        for (int i = 0; i < taskInfoIds.length; i++) {
            boolean isMatching = true;
            for (int j = 0; j < filters.length; j++) {
                if (!filters[j].isMatching(taskInfoIds[i].taskInfo)){
                    isMatching = false;
                    break;
                }
            }
            if (isMatching)
                filteredList.add(taskInfoIds[i]);
        }
        
        TaskInfoId[] result = new TaskInfoId[filteredList.size()];
        filteredList.toArray(result);
        return result;
    }
    
    private SearchResult searchWithSuggestion(Filter[] filters) {
        Set<TaskId> taskIds = findMatchingTasks(filters);
        
        updateSearchedTasks(taskIds);
        
        lastSearchedTasks = splitAll(lastSearchedTasks);
        lastSearchedTasks = refilter(filters, lastSearchedTasks);
        sortTasks(lastSearchedTasks);
        
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
    
    private SearchResult searchWithSuggestionWithoutSplit(Filter[] filters) {
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
    
    private SearchResult searchAndUpdate(Filter[] filters) {
        Set<TaskId> taskIds = findMatchingTasks(filters);
        updateSearchedTasks(taskIds);
        
        lastSearchedTasks = splitAll(lastSearchedTasks);
        lastSearchedTasks = refilter(filters, lastSearchedTasks);
        
        sortTasks(lastSearchedTasks);
        
        SearchResult result = new SearchResult(getInfoArray(lastSearchedTasks), 
                getIdArray(lastSearchedTasks), filters);
        
        lastSearchedSuggestions = null;
        return result;
    }
    
    private SearchResult searchAndUpdateWithoutSplit(Filter[] filters) {
        Set<TaskId> taskIds = findMatchingTasks(filters);
        updateSearchedTasks(taskIds);
        
        SearchResult result = new SearchResult(getInfoArray(lastSearchedTasks), 
                getIdArray(lastSearchedTasks), filters);
        
        lastSearchedSuggestions = null;
        return result;
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
    
    public Result searchTasksWithoutSplit(Filter[] filters) {
        assert filters != null : "filters can't be null";
        log.log(Level.FINER, "Conduct search: " + filters.length + " filters");
        
        SearchResult result = searchAndUpdateWithoutSplit(filters);
        
        if (result.getTaskIds().length == 0) {
            Filter[] newFilters = 
                    suggestionFinder.generateSuggestionFilters(filters);
            if (newFilters == null) {
                return result;
            } else {
                return searchWithSuggestionWithoutSplit(newFilters);
            }
        } else {
            return result;
        }
    }
    
    public SearchResult getLastSearchResult() {
        SearchResult result = new SearchResult(getInfoArray(lastSearchedTasks),
                getIdArray(lastSearchedTasks), null);
        result.setSuggestion(lastSearchedSuggestions);

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
    
    public Result details(TaskIdSet taskIdSet) {
        int size = taskIdSet.size();
        TaskId[] taskIds = new TaskId[size];
        TaskInfo[] taskInfos = new TaskInfo[size];
        
        int index = 0;
        for (TaskId taskId : taskIdSet) {
            taskIds[index] = taskId;
            taskInfos[index] = taskData.getTaskInfo(taskId);
            index++;
        }
        
        return new DetailsResult(taskInfos, taskIds);
    }

    public TaskId getAbsoluteIndex(int relativeIndex) {
        if (relativeIndex > lastSearchedTasks.length) {
            throw new IndexOutOfBoundsException();
        }
        
        TaskId taskId = lastSearchedTasks[relativeIndex - 1].taskId;
        if (taskData.taskExists(taskId)) {
            return taskId;
        } else {
            return null;
        }
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