package main.command;

import main.response.Response;
import manager.StateManager;
import manager.datamanager.DeleteManager;
import manager.datamanager.SearchManager;

public class DeleteCommand implements Command {
    private final DeleteManager deleteManager;
    private final SearchManager searchManager;
    private final StateManager stateManager;

    public DeleteCommand(DeleteManager deleteManager,
            SearchManager searchManager, StateManager stateManager) {
        this.deleteManager = deleteManager;
        this.searchManager = searchManager;
        this.stateManager = stateManager;
    }

    @Override
    public Response execute() {
        // TODO Auto-generated method stub
        return null;
    }

}
