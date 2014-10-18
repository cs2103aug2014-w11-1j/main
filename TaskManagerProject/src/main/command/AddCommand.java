package main.command;

import main.command.parser.CommandParser;
import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.AddManager;
import manager.result.Result;
import data.taskinfo.TaskInfo;

public class AddCommand extends Command {
    private final AddManager addManager;
    private final StateManager stateManager;
    private final TaskInfo taskToAdd;

    public AddCommand(String args, ManagerHolder managerHolder) {
        super(managerHolder);
        addManager = managerHolder.getAddManager();
        stateManager = managerHolder.getStateManager();
        
    	taskToAdd = parse(args);
    }

    private TaskInfo parse(String args) {
        TaskInfo newTask = CommandParser.parseTask(args);
        return newTask;
    }

    @Override
    protected boolean isValidArguments() {
        return true;
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
