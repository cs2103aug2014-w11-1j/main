package main.command;

import main.response.Response;
import manager.datamanager.AddManager;
import manager.datamanager.SearchManager;

public class AddCommand implements Command {
    private final AddManager addManager;
    private final SearchManager searchManager;

    public AddCommand(AddManager addManager, SearchManager searchManager) {
        this.addManager = addManager;
        this.searchManager = searchManager;
    }

    @Override
    public Response execute() {
        // TODO Auto-generated method stub
        return null;
    }

}
