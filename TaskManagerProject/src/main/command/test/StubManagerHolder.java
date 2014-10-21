package main.command.test;

import main.command.TaskIdSet;
import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.AddManager;
import manager.datamanager.DeleteManager;
import manager.datamanager.EditManager;
import manager.datamanager.FreeDaySearchManager;
import manager.datamanager.SearchManager;
import manager.datamanager.searchfilter.Filter;
import manager.result.Result;
import data.TaskId;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

class StubManagerHolder extends ManagerHolder {
    public StubManagerHolder() {
        super(null, null);
    }

    @Override
    public StateManager getStateManager() {
        return new StubStateManager();
    }

    @Override
    public AddManager getAddManager() {
        return new StubAddManager();
    }

    @Override
    public SearchManager getSearchManager() {
        return new StubSearchManager();
    }

    @Override
    public EditManager getEditManager() {
        return new StubEditManager();
    }

    @Override
    public DeleteManager getDeleteManager() {
        return new StubDeleteManager();
    }

    @Override
    public FreeDaySearchManager getFreeDaySearchManager() {
        return new StubFreeDaySearchManager();
    }

}

class StubAddManager extends AddManager {
    TaskInfo taskInfo;

    public StubAddManager() {
        super(null);
    }

    @Override
    public Result addTask(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
        return null;
    }

    public TaskInfo getTaskInfo() {
        return taskInfo;
    }
}

class StubDeleteManager extends DeleteManager {
    public StubDeleteManager() {
        super(null);
    }

    @Override
    public Result deleteTask(TaskIdSet taskIdSet) {
        return null;
    }
}

class StubEditManager extends EditManager {
    public StubEditManager() {
        super(null);
    }

    @Override
    public Result startEditMode(TaskIdSet taskIdSet) {
        return null;
    }

    @Override
    public Result addTaskTags(Tag[] tags, TaskIdSet taskIdSet) {
        return null;
    }

    @Override
    public Result deleteTaskTags(Tag[] tags, TaskIdSet taskIdSet) {
        return null;
    }

    @Override
    public Result editTask(TaskInfo taskInfo, TaskIdSet taskIdSet) {
        return null;
    }
}

class StubFreeDaySearchManager extends FreeDaySearchManager {
    public StubFreeDaySearchManager() {
        super(null);
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

    @Override
    public Result searchTasks(Filter[] filters) {
        return null;
    }

    @Override
    public Result details(TaskId taskId) {
        return null;
    }
}

class StubStateManager extends StateManager {
    public StubStateManager() {
        super(null, null, null);
    }

    @Override
    public boolean canQuerySearchManager() {
        return true;
    }

}