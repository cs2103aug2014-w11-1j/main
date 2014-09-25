package main.command;

import main.response.Response;
import manager.StateManager;
import manager.datamanager.SearchManager;

public class SearchCommand implements Command {
    private final SearchManager searchManager;
    private final StateManager stateManager;

    public SearchCommand(SearchManager searchManager, StateManager stateManager) {
        this.searchManager = searchManager;
        this.stateManager = stateManager;
    }

    @Override
    public Response execute() {
        // TODO Auto-generated method stub

        stateManager.beforeCommandExecutionUpdate();
        
        return null;
    }

}
