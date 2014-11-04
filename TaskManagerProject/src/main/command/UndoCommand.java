package main.command;

import manager.ManagerHolder;
import manager.datamanager.UndoManager;
import manager.result.Result;

//@author A0065475X
public class UndoCommand extends Command {
    private final UndoManager undoManager;

    public UndoCommand(ManagerHolder managerHolder) {
        super(managerHolder);
        undoManager = managerHolder.getUndoManager();
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
        Result result = undoManager.undo();
        return result;
    }

}
