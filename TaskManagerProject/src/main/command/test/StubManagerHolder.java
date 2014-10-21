package main.command.test;

import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.SearchManager;
import data.TaskId;

class StubManagerHolder extends ManagerHolder {

    public StubManagerHolder() {
        super(null, null);
    }

    @Override
    public SearchManager getSearchManager() {
        return new StubSearchManager();
    }

    @Override
    public StateManager getStateManager() {
        return new StubStateManager();
    }

}

class StubSearchManager extends SearchManager {

    public StubSearchManager() {
        super(null);
    }

    @Override
    public TaskId getAbsoluteIndex(int relativeTaskId) {
        return new TaskId(relativeTaskId);
    }
}

class StubStateManager extends StateManager {

    public StubStateManager() {
        super(null, null, null);
    }

    @Override
    public boolean canQueryStateManager() {
        return true;
    }

}