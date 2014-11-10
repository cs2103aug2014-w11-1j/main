package main.command;

import java.time.LocalDate;
import java.time.LocalTime;

import main.command.parser.CommandParser;
import main.command.parser.DateTimePair;
import manager.ManagerHolder;
import manager.datamanager.AddManager;
import manager.result.Result;
import data.taskinfo.Priority;
import data.taskinfo.TaskInfo;

//@author A0111862M
/**
 * Command class for Add operations. Parses given command arguments into
 * segments of a task and stores them, passing them onto the AddManager class
 * when the Command is executed.
 */
public class AddCommand extends Command {
    private final AddManager addManager;
    private final TaskInfo taskToAdd;

    public AddCommand(String args, ManagerHolder managerHolder) {
        super(managerHolder);
        addManager = managerHolder.getAddManager();

        taskToAdd = parse(args);
    }

    /**
     * Parses command arguments into different parts of a task and sets them
     * into a TaskInfo object.
     *
     * @param cmdArgs
     *            the arguments for the add command
     * @return the TaskInfo object with the task parts set.
     */
    private TaskInfo parse(String cmdArgs) {
        TaskInfo task = TaskInfo.create();

        task.name = CommandParser.parseName(cmdArgs);
        parseDateTimes(cmdArgs, task);
        task.tags = CommandParser.parseTags(cmdArgs);
        Priority p = CommandParser.parsePriority(cmdArgs);
        if (p != null) {
            task.priority = p;
        }

        return task;
    }

    /**
     * Parses a command string into dates and times suitable for the add
     * command.
     * <p>
     * If more than 2 pairs of dates and times are found, only the first two are
     * used. All combinations are allowed aside from a date range without any
     * time indicated.
     *
     * @param cmdArgs
     *            the arguments possibly containing dates and times
     * @param task
     *            the task to set the dates and times into
     */
    private void parseDateTimes(String cmdArgs, TaskInfo task) {
        DateTimePair range = CommandParser.parseDateTimesInSequence(cmdArgs);
        if (range.isEmpty()) {
            return;
        }
        if (range.getNumOfDates() == 2 && range.getNumOfTimes() == 0) {
            return;
        }

        // store times first
        if (!range.hasSecondTime()) {
            task.endTime = range.getFirstTime();
        } else {
            task.startTime = range.getFirstTime();
            task.endTime = range.getSecondTime();
        }

        // two dates, use them accordingly
        if (range.hasFirstDate() && range.hasSecondDate()) {
            task.startDate = range.getFirstDate();
            task.endDate = range.getSecondDate();

            // only 1 time: duplicate first time.
            if (!range.hasSecondTime()) {
                task.startTime = range.getFirstTime();
            }
        }

        // one date, use the same for both
        if (range.hasFirstDate() != range.hasSecondDate()) {
            task.startDate = task.endDate = range.hasFirstDate() ? range
                    .getFirstDate() : range.getSecondDate();

            // there can only be one date if there is only one time
            if (!range.hasSecondTime()) {
                task.startDate = null;
            }
        }

        // no date, get the next possible date for the times
        if (!range.hasFirstDate() && !range.hasSecondDate()) {
            task.startDate = task.endDate = getNextOccurrenceOfTime(
                    range.getFirstTime(), LocalTime.now(), LocalDate.now());
            if (range.hasSecondTime()) {
                task.endDate = getNextOccurrenceOfTime(range.getSecondTime(),
                        range.getFirstTime(), task.startDate);
            }

            // there can only be one date if there is only one time
            if (!range.hasSecondTime()) {
                task.startDate = null;
            }
        }
    }

    /**
     * Returns the next occurrence of {@code time} from a reference datetime
     * constructed from {@code timeFrom} and {@code dateFrom}.
     *
     * @param time
     *            the required time
     * @param timeFrom
     *            the reference time
     * @param dateFrom
     *            the reference date
     * @return the next occurrence of time from the reference datetime
     */
    private LocalDate getNextOccurrenceOfTime(LocalTime time,
            LocalTime timeFrom, LocalDate dateFrom) {
        if (time.isAfter(timeFrom)) {
            return dateFrom;
        } else {
            return dateFrom.plusDays(1);
        }
    }

    @Override
    protected boolean isValidArguments() {
        return (taskToAdd != null && taskToAdd.isValid());
    }

    @Override
    protected boolean isCommandAllowed() {
        return stateManager.canAdd();
    }

    @Override
    protected Result executeAction() {
        Result result = addManager.addTask(taskToAdd);
        return result;
    }

}
