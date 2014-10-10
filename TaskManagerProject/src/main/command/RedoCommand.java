package main.command;

import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.UndoManager;
import manager.result.Result;

public class RedoCommand extends Command {
    private final UndoManager undoManager;
    private final StateManager stateManager;

    public RedoCommand(ManagerHolder managerHolder) {
        super(managerHolder);
        undoManager = managerHolder.getUndoManager();
        stateManager = managerHolder.getStateManager();
    }

    @Override
    protected boolean isValidArguments() {
        return true;
    }

    @Override
    protected boolean isCommandAllowed() {
        return stateManager.canUndo();
    }

    @Override
    protected Result executeAction() {
        Result result = undoManager.redo();
        return result;
    }

}
