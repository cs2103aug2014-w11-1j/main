package main.command;

import manager.ManagerHolder;
import manager.datamanager.UndoManager;
import manager.result.Result;

public class RedoCommand extends Command {
    private final UndoManager undoManager;
    int times;

    public RedoCommand(String args, ManagerHolder managerHolder) {
        super(managerHolder);
        undoManager = managerHolder.getUndoManager();
        
        parse(args);
    }
    
    private void parse(String args) {
        if (args.isEmpty()) {
            times = 1;
        } else {
            try {
                times = Integer.parseInt(args);
            } catch (NumberFormatException e) {
                times = -1;
            }
        }
    }

    @Override
    protected boolean isValidArguments() {
        return times >= 1;
    }

    @Override
    protected boolean isCommandAllowed() {
        return stateManager.canUndo();
    }

    @Override
    protected Result executeAction() {
        Result result = undoManager.redo(times);
        return result;
    }

}
