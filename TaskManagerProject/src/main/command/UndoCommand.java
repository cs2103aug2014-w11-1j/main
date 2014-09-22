package main.command;

import main.response.Response;
import manager.datamanager.UndoManager;

public class UndoCommand implements Command {

    private final UndoManager undoManager;

    public UndoCommand(UndoManager undoManager) {
        this.undoManager = undoManager;
    }

    @Override
    public Response execute() {
        // TODO Auto-generated method stub
        return null;
    }

}
