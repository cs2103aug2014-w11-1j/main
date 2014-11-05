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

public class AddCommand extends Command {
    private final AddManager addManager;
    private final TaskInfo taskToAdd;

    public AddCommand(String args, ManagerHolder managerHolder) {
        super(managerHolder);
        addManager = managerHolder.getAddManager();

    	taskToAdd = parse(args);
    }

    private TaskInfo parse(String args) {
        TaskInfo task = TaskInfo.create();

        task.name = CommandParser.parseName(args, false);
        parseDateTimes(args, task);
        task.tags = CommandParser.parseTags(args);
        Priority p = CommandParser.parsePriority(args);
        if (p != null) {
            task.priority = p;
        }

        return task;
    }

    private void parseDateTimes(String args, TaskInfo task) {
        DateTimePair range = CommandParser.parseDateTimesInSequence(args);
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
            task.startDate = task.endDate = range.hasFirstDate() ?
                range.getFirstDate() : range.getSecondDate();

            // there can only be one date if there is only one time
            if (!range.hasSecondTime()) {
                task.startDate = null;
            }
        }

        // no date, get the next possible date for the times
        if (!range.hasFirstDate() && !range.hasSecondDate()) {
            task.startDate = task.endDate = getNextOccurrence(
                range.getFirstTime(), LocalTime.now(), LocalDate.now());
            if (range.hasSecondTime()) {
                task.endDate = getNextOccurrence(
                    range.getSecondTime(), range.getFirstTime(), task.startDate);
            }
            
            // there can only be one date if there is only one time
            if (!range.hasSecondTime()) {
                task.startDate = null;
            }
        }
    }

    private LocalDate getNextOccurrence(LocalTime time, LocalTime timeFrom, LocalDate dateFrom) {
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
