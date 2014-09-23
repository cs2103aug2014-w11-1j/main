package main.command;

import main.response.Response;
import manager.StateManager;
import manager.datamanager.AddManager;
import manager.datamanager.SearchManager;

public class AddCommand implements Command {
    private final AddManager addManager;
    private final SearchManager searchManager;
    private final StateManager stateManager;

    public AddCommand(AddManager addManager, SearchManager searchManager,
            StateManager stateManager) {
        this.addManager = addManager;
        this.searchManager = searchManager;
        this.stateManager = stateManager;
    }

    @Override
    public Response execute() {
        // TODO Auto-generated method stub
        return null;
    }

}
