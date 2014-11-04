package main.command;

import java.time.LocalDate;

import main.command.parser.CommandParser;
import main.command.parser.DateTimePair;
import manager.ManagerHolder;
import manager.datamanager.freetimemanager.FreeTimeSearchManager;
import manager.result.Result;

public class FreeTimeSearchCommand extends Command {

	private FreeTimeSearchManager freeTimeSearchManager;
	private LocalDate date;
	
	public FreeTimeSearchCommand(String args, ManagerHolder managerHolder) {
		super(managerHolder);
		this.freeTimeSearchManager = managerHolder.getFreeTimeSearchManager();
		parse(args);
	}
	
	private void parse(String args) {
		DateTimePair dateTimePair = CommandParser.parseDateTimes(args);
		
		if (dateTimePair.getNumOfDates() == 1 && 
		        dateTimePair.getNumOfTimes() == 0) {
		    
		    assert dateTimePair.getSecondDate() == null;
		    date = dateTimePair.getFirstDate();
		} else {
		    date = null;
		}
	}

	@Override
	protected boolean isValidArguments() {
	    return date != null;
	}

	@Override
	protected boolean isCommandAllowed() {
	    return stateManager.canSearch();
	}

	@Override
	protected Result executeAction() {
	    Result result = freeTimeSearchManager.searchFreeTimeSlot(date);
	    return result;
	}
	
}
