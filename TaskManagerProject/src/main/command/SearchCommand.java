package main.command;

import main.response.Response;
import manager.datamanager.SearchManager;

public class SearchCommand implements Command {
    private final SearchManager searchManager;

    public SearchCommand(SearchManager searchManager) {
        this.searchManager = searchManager;
    }

    @Override
    public Response execute() {
        // TODO Auto-generated method stub
        return null;
    }

}
