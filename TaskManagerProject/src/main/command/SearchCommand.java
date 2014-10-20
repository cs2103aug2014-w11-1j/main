package main.command;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import main.command.parser.CommandParser;
import main.command.parser.DateTimePair;
import manager.ManagerHolder;
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
    private final List<Filter> filterList;

    public SearchCommand(String args, ManagerHolder managerHolder) {
        super(managerHolder);
        searchManager = managerHolder.getSearchManager();

        filterList = new ArrayList<Filter>();
        parse(args);
    }

    private void parse(String args) {
        assert args != null : "There should not be a null passed in.";
        if (args.isEmpty()) {
            return;
        }

        // TODO set up a way to have a common repository of symbols, like DELIM
        String taskName = CommandParser.parseName(args);
        if (!taskName.isEmpty()) {
            String delim = " ";
            String[] keywords = taskName.split(delim);
            filterList.add(new KeywordFilter(keywords));
        }

        List<LocalDateTime> dateRange = parseDateTimes(args);
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

    private List<LocalDateTime> parseDateTimes(String args) {
        DateTimePair range = CommandParser.parseDateTimes(args);
        if (range.isEmpty()) {
            return null;
        }

        LocalDate startDate = range.getFirstDate();
        LocalTime startTime = range.getFirstTime();
        LocalDate endDate = range.getSecondDate();
        LocalTime endTime = range.getSecondTime();

        if (!range.hasFirstDate()) {
            if (range.hasSecondDate()) {
                startDate = endDate;
            } else {
                startDate = LocalDate.now();
            }
        }

        if (!range.hasSecondTime()) {
            if (range.hasFirstTime() && !range.hasSecondDate()) {
                endTime = startTime;
            } else {
                endTime = LocalTime.MAX;
            }
        }

        if (!range.hasSecondDate()) {
            endDate = startDate;
        }

        if (!range.hasFirstTime()) {
            startTime = LocalTime.MIN;
        }

        List<LocalDateTime> dtRange = new ArrayList<LocalDateTime>();
        dtRange.add(LocalDateTime.of(startDate, startTime));
        dtRange.add(LocalDateTime.of(endDate, endTime));

        return dtRange;
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
