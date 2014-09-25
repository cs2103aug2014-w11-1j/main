package main.command;

import main.response.Response;
import manager.StateManager;
import manager.datamanager.EditManager;
import manager.datamanager.SearchManager;

public class EditCommand implements Command {
    private final EditManager editManager;
    private final SearchManager searchManager;
    private final StateManager stateManager;

    public EditCommand(EditManager editManager, SearchManager searchManager,
            StateManager stateManager) {
        this.editManager = editManager;
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
