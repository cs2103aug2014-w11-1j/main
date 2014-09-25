package main.command;

import main.response.EnumResponse;
import main.response.Response;
import manager.StateManager;
import manager.datamanager.UndoManager;
import manager.result.Result;

public class UndoCommand implements Command {

    private final UndoManager undoManager;
    private final StateManager stateManager;

    public UndoCommand(UndoManager undoManager, StateManager stateManager) {
        this.undoManager = undoManager;
        this.stateManager = stateManager;
    }

    @Override
    public Response execute() {
        
        if (stateManager.canUndo()) {
            Result result = undoManager.undo();
            Response response = stateManager.update(result);
            return response;
            
        } else {
            return EnumResponse.cannotExecuteCommand();
        }
    }

}
