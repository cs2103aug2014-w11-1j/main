package main.command;

import main.response.Response;
import manager.datamanager.EditManager;
import manager.datamanager.SearchManager;

public class EditCommand implements Command {
    private final EditManager editManager;
    private final SearchManager searchManager;

    public EditCommand(EditManager editManager, SearchManager searchManager) {
        this.editManager = editManager;
        this.searchManager = searchManager;
    }

    @Override
    public Response execute() {
        // TODO Auto-generated method stub
        return null;
    }

}
