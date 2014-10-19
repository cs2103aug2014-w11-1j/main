package main.command;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import main.command.parser.CommandParser;
import main.command.parser.DateParser;
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

public class SearchCommand extends Command {
    private final SearchManager searchManager;
    private final StateManager stateManager;
    private final List<Filter> filterList;

    public SearchCommand(String args, ManagerHolder managerHolder) {
        super(managerHolder);
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
        String taskName = CommandParser.parseName(args);
        String[] keywords = taskName.split(delim);
        if (keywords != null) {
            filterList.add(new KeywordFilter(keywords));
        }

        // TODO refactor date parser further
        List<LocalDateTime> dateRange = DateParser.parseDateTime(args);
        if (dateRange != null) {
            filterList.add(new DateTimeFilter(dateRange.get(0), dateRange.get(1)));
        }

        Tag[] tags = CommandParser.parseTags(args);
        if (tags != null) {
            filterList.add(new TagFilter(tags));
        }

        Priority[] priorities = CommandParser.parsePriorities(args);
        if (priorities != null) {
            filterList.add(new PriorityFilter(priorities));
        }
    }

    @Override
    protected boolean isValidArguments() {
        return true;
    }

    @Override
    protected boolean isCommandAllowed() {
        return stateManager.canSearch();
    }

    @Override
    protected Result executeAction() {
        Filter[] filters = filterList.toArray(new Filter[filterList.size()]);
        Result result = searchManager.searchTasks(filters);
        return result;
    }

}
