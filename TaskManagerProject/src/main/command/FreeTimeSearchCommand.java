package main.command;

import java.time.LocalDate;

import javax.xml.bind.ParseConversionEvent;

import main.command.parser.CommandParser;
import main.command.parser.DateTimePair;
import manager.ManagerHolder;
import manager.datamanager.freetimemanager.FreeTimeSearchManager;
import manager.result.Result;

public class FreeTimeSearchCommand extends Command {

	private FreeTimeSearchManager freeTimeSearchManager;
	private DateTimePair date;
	
	public FreeTimeSearchCommand(String args, ManagerHolder managerHolder) {
		super(managerHolder);
		this.freeTimeSearchManager = managerHolder.getFreeTimeSearchManager();
		parse(args);
	}
	
	private void parse(String args) {
		date = CommandParser.parseDateTimes(args);
	}

	@Override
	protected boolean isValidArguments() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean isCommandAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Result executeAction() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
