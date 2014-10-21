package main.command;

import main.command.parser.CommandParser;
import main.command.parser.DateTimePair;
import manager.ManagerHolder;
import manager.datamanager.FreeTimeSlotManager;
import manager.result.Result;

public class FreeDaySearchCommand extends Command {
    private FreeTimeSlotManager freeDaySearchManager;
    private DateTimePair dateTimePair;

    public FreeDaySearchCommand(String args, ManagerHolder managerHolder) {
        super(managerHolder);
        this.freeDaySearchManager = managerHolder.getFreeDaySearchManager();
        parse(args);
    }

    private void parse(String args) {
        dateTimePair = CommandParser.parseDateTimes(args);
    }

    @Override
    protected boolean isValidArguments() {
        if (dateTimePair == null) {
            return false;
        }
        if (!dateTimePair.hasFirstDate() || !dateTimePair.hasSecondDate()) {
            return false;
        }
        if (dateTimePair.hasFirstTime() != dateTimePair.hasSecondTime()) {
            return false;
        }
        return true;
    }

    @Override
    protected boolean isCommandAllowed() {
        return stateManager.canSearch();
    }

    @Override
    protected Result executeAction() {
        if (dateTimePair.hasFirstTime()) {
            return freeDaySearchManager.searchFreeTimeSlot(
                    dateTimePair.getFirstTime(),
                    dateTimePair.getFirstDate(),
                    dateTimePair.getSecondTime(),
                    dateTimePair.getSecondDate());
        } else {
            return freeDaySearchManager.searchFreeDay(
                    dateTimePair.getFirstDate(),
                    dateTimePair.getSecondDate());
        }
    }

}
