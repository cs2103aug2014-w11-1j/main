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
import manager.datamanager.searchfilter.KeywordFilter;
import manager.datamanager.searchfilter.PriorityFilter;
import manager.datamanager.searchfilter.TagFilter;
import manager.result.Result;
import data.taskinfo.Priority;
import data.taskinfo.Tag;

public class SearchCommand implements Command {
    private final SearchManager searchManager;
    private final StateManager stateManager;
    private final List<Filter> filterList;

    public SearchCommand(String args, ManagerHolder managerHolder) {
        searchManager = managerHolder.getSearchManager();
        stateManager = managerHolder.getStateManager();
        
        filterList = new ArrayList<Filter>();
        parse(args);
    }

    private void parse(String args) {
        assert args != null : "There should not be a null passed in.";
        if (args.isEmpty()) {
            return;
        }

        // TODO remove dates, tags, and priorities from keywords
        String delim = " ";
        String[] keywords = args.split(delim);
        if (keywords != null) {
            filterList.add(new KeywordFilter(keywords));
        }

        // TODO refactor date parser further
        List<LocalDateTime> dateRange = DateParser.parseDateTime(args);
        if (dateRange != null) {
            filterList.add(new DateTimeFilter(dateRange.get(0), dateRange.get(1)));
        }

        Tag[] newTags = CommandParser.parseTags(args);
        if (newTags != null) {
            filterList.add(new TagFilter(newTags));
        }

        Priority newPriority = CommandParser.parsePriority(args);
        if (newPriority != null) {
            // TODO support for multiple priorities
            Priority[] pArr = {newPriority};
            filterList.add(new PriorityFilter(pArr));
        }
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
