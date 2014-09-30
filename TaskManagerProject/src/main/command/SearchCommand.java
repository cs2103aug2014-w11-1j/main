package main.command;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import main.message.EnumMessage;
import main.modeinfo.EmptyModeInfo;
import main.response.Response;
import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.SearchManager;
import manager.datamanager.searchfilter.DateTimeFilter;
import manager.datamanager.searchfilter.Filter;
import manager.datamanager.searchfilter.PriorityFilter;
import manager.datamanager.searchfilter.TagFilter;
import manager.result.Result;
import data.taskinfo.Priority;
import data.taskinfo.TaskInfo;

public class SearchCommand implements Command {
    private final SearchManager searchManager;
    private final StateManager stateManager;
    private final TaskInfo searchTask;
    private final List<Filter> filterList = new ArrayList<Filter>();

    public SearchCommand(String args, ManagerHolder managerHolder) {
        searchTask = parse(args);
        searchManager = managerHolder.getSearchManager();
        stateManager = managerHolder.getStateManager();
    }

    private TaskInfo parse(String args) {
        TaskInfo searchCriteria = CommandParser.parseTask(args);
        // TODO search name - confirm on name format
        // TODO search within day range - refactor date parser
        if (searchCriteria.duration != null) {
            LocalDateTime endDateTime = LocalDateTime.of(searchCriteria.endDate, searchCriteria.endTime);
            LocalDateTime startDateTime = endDateTime.minus(searchCriteria.duration);
            filterList.add(new DateTimeFilter(startDateTime, endDateTime));
        }
        if (searchCriteria.tags != null) {
            filterList.add(new TagFilter(searchCriteria.tags));
        }
        if (searchCriteria.priority != null) {
            // TODO support for multiple priorities
            Priority[] pArr = {searchCriteria.priority};
            filterList.add(new PriorityFilter(pArr));
        }
        return searchCriteria;
    }

    @Override
    public Response execute() {
        if (stateManager.canSearch()) {
            stateManager.beforeCommandExecutionUpdate();

            Result result = searchManager.searchTasks(
                    filterList.toArray(new Filter[filterList.size()]));
            Response response = stateManager.update(result);
            return response;
        } else {
            EnumMessage message = EnumMessage.cannotExecuteCommand();
            EmptyModeInfo modeInfo = new EmptyModeInfo();
            return new Response(message, modeInfo);
        }
    }

}
