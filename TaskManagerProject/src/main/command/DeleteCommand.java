package main.command;

import main.response.Response;
import manager.datamanager.DeleteManager;
import manager.datamanager.SearchManager;

public class DeleteCommand implements Command {
    private final DeleteManager deleteManager;
    private final SearchManager searchManager;

    public DeleteCommand(DeleteManager deleteManager, SearchManager searchManager) {
        this.deleteManager = deleteManager;
        this.searchManager = searchManager;
    }

    @Override
    public Response execute() {
        // TODO Auto-generated method stub
        return null;
    }

}
