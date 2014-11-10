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
import manager.datamanager.searchfilter.StatusFilter;
import manager.datamanager.searchfilter.TagFilter;
import manager.result.Result;
import data.taskinfo.Priority;
import data.taskinfo.Status;
import data.taskinfo.Tag;

//@author A0111862M
/**
 * Command class for Search operations. Parses given command arguments into
 * segments of a task and stores them as a list of filters, passing them onto
 * the SearchManager class when the Command is executed.
 */
public class SearchCommand extends Command {
    private final SearchManager searchManager;
    private final List<Filter> filterList;

    public SearchCommand(String args, ManagerHolder managerHolder) {
        super(managerHolder);
        searchManager = managerHolder.getSearchManager();

        filterList = new ArrayList<Filter>();
        parse(args);
    }

    /**
     * Parses command arguments into different parts of a task and sets them
     * into a list of filters stored in the SearchCommand.
     *
     * @param cmdArgs
     *            the arguments for the search command
     */
    private void parse(String cmdArgs) {
        assert cmdArgs != null : "There should not be a null passed in.";
        if (cmdArgs.isEmpty()) {
            filterList.add(StatusFilter.makeDefault());
            return;
        }

        String taskName = CommandParser.parseName(cmdArgs);
        if (!taskName.isEmpty()) {
            String delim = " ";
            String[] keywords = taskName.split(delim);
            filterList.add(new KeywordFilter(keywords));
        }

        List<LocalDateTime> dateTimeRange = parseDateTimes(cmdArgs);
        if (dateTimeRange != null) {
            LocalDateTime startDateTime = dateTimeRange.get(0);
            LocalDateTime endDateTime = dateTimeRange.get(1);
            filterList.add(new DateTimeFilter(startDateTime, endDateTime));
        }

        Tag[] tags = CommandParser.parseTags(cmdArgs);
        if (tags != null) {
            filterList.add(new TagFilter(tags));
        }

        Priority[] priorities = CommandParser.parsePriorities(cmdArgs);
        if (priorities != null) {
            filterList.add(new PriorityFilter(priorities));
        }

        Status[] statuses = CommandParser.parseStatuses(cmdArgs);
        if (statuses != null) {
            filterList.add(new StatusFilter(statuses));
        } else {
            filterList.add(StatusFilter.makeDefault());
        }
    }

    /**
     * Parses a command string into dates and times suitable for the search
     * command.
     * <p>
     * If more than 2 pairs of dates and times are found, only the first two are
     * used. Combinations are handled as shown below:
     * <ul>
     *  <li>1 date - Set the start and end date to that date, times to the start
     *      and end of the day.</li>
     *  <li>1 time - Set the start and end time to that time, dates to the
     *      current date.</li>
     *  <li>1 date and time - Set the end date and time to those.</li>
     *  <li>1 date and 2 times - Set the start and end date to that date, times
     *      to the start and end times accordingly.</li>
     *  <li>2 dates and 1 time - Set the dates to the start and end dates
     *      accordingly, set the start time to the start of the day, end time to
     *      the end of the day, then the time to the start or end time according
     *      to its position.</li>
     *  <li>2 dates and times each - Set the start and end dates and times
     *      according to the positions.</li>
     * </ul>
     *
     * @param cmdArgs
     *            the arguments possibly containing dates and times
     * @param task
     *            the task to set the dates and times into
     * @return a list of LocalDateTime, the first element indicating the start
     *            datetime, the second indicating the end datetime; or null if
     *            no dates or times are found
     */
    private List<LocalDateTime> parseDateTimes(String args) {
        DateTimePair range = CommandParser.parseDateTimesInSequence(args);
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
