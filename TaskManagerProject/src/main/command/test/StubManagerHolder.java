package main.command.test;

import main.command.EditCommand.Info;
import main.command.TargetedCommand;
import main.command.TaskIdSet;
import main.response.Response;
import manager.ManagerHolder;
import manager.StateManager;
import manager.datamanager.AddManager;
import manager.datamanager.DeleteManager;
import manager.datamanager.EditManager;
import manager.datamanager.FreeDaySearchManager;
import manager.datamanager.SearchManager;
import manager.datamanager.searchfilter.Filter;
import manager.result.Result;
import manager.result.SearchResult;
import data.TaskId;
import data.taskinfo.Tag;
import data.taskinfo.TaskInfo;

/**
 * A set of stubs for testing the Commands
 */
//@author A0065475X
class StubManagerHolder extends ManagerHolder {
    private StubAddManager stubAddManager;
    private StubSearchManager stubSearchManager;
    private StubStateManager stubStateManager;
    private StubEditManager stubEditManager;

    public StubManagerHolder() {
        super(null, null, null, null);
        stubAddManager = new StubAddManager();
        stubSearchManager = new StubSearchManager();
        stubStateManager = new StubStateManager();
        stubEditManager = new StubEditManager();
    }

    @Override
    public StateManager getStateManager() {
        return stubStateManager;
    }

    @Override
    public StubAddManager getAddManager() {
        return stubAddManager;
    }

    @Override
    public StubSearchManager getSearchManager() {
        return stubSearchManager;
    }

    @Override
    public EditManager getEditManager() {
        return stubEditManager;
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
    public TaskInfo taskInfo;

    public StubAddManager() {
        super(null);
    }

    @Override
    public Result addTask(TaskInfo taskInfo) {
        this.taskInfo = taskInfo;
        return null;
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
    public TaskIdSet lastTaskIdSet;
    public Tag[] lastTags;
    public TaskInfo lastTaskInfo;
    public Method lastMethodCall;
    public Info lastInfoToClear;

    public enum Method {
        START_EDIT_MODE,
        ADD_TASK_TAGS,
        DELETE_TASK_TAGS,
        EDIT_TASK,
        CLEAR_INFO
    }

    public StubEditManager() {
        super(null);
    }

    public void clearMemory() {
        lastTaskIdSet = null;
        lastTags = null;
        lastTaskInfo = null;
        lastMethodCall = null;
    }

    @Override
    public Result startEditMode(TaskIdSet taskIdSet) {
        clearMemory();
        lastMethodCall = Method.START_EDIT_MODE;
        lastTaskIdSet = taskIdSet;
        return null;
    }

    @Override
    public Result addTaskTags(Tag[] tags, TaskIdSet taskIdSet) {
        clearMemory();
        lastMethodCall = Method.ADD_TASK_TAGS;
        lastTaskIdSet = taskIdSet;
        lastTags = tags;
        return null;
    }

    @Override
    public Result deleteTaskTags(Tag[] tags, TaskIdSet taskIdSet) {
        clearMemory();
        lastMethodCall = Method.DELETE_TASK_TAGS;
        lastTaskIdSet = taskIdSet;
        lastTags = tags;
        return null;
    }

    @Override
    public Result editTask(TaskInfo taskInfo, TaskIdSet taskIdSet) {
        clearMemory();
        lastMethodCall = Method.EDIT_TASK;
        lastTaskIdSet = taskIdSet;
        lastTaskInfo = taskInfo;
        return null;
    }

    @Override
    public Result clearInfo(TaskIdSet taskIdSet, Info infoToClear) {
        clearMemory();
        lastMethodCall = Method.CLEAR_INFO;
        lastTaskIdSet = taskIdSet;
        lastInfoToClear = infoToClear;
        return null;
    }

}

class StubFreeDaySearchManager extends FreeDaySearchManager {
    public StubFreeDaySearchManager() {
        super(null);
    }
}

class StubSearchManager extends SearchManager {
    public Filter[] filters;

    public StubSearchManager() {
        super(null);
    }

    @Override
    public TaskId getAbsoluteIndex(int relativeTaskId) {
        return new TaskId(relativeTaskId);
    }

    @Override
    public Result searchTasks(Filter[] filters) {
        this.filters = filters;

        TaskInfo[] tasks = new TaskInfo[0];
        TaskId[] taskIds = new TaskId[0];
        return new SearchResult(tasks, taskIds, filters);
    }
    
    @Override
    public Result searchTasksWithoutSplit(Filter[] filters) {
        this.filters = filters;
        
        TaskInfo[] tasks = new TaskInfo[0];
        TaskId[] taskIds = new TaskId[0];
        return new SearchResult(tasks, taskIds, filters);
    }

    @Override
    public Result details(TaskIdSet taskIdSet) {
        return null;
    }
}

class StubStateManager extends StateManager {
    public boolean canEdit = true;
    public boolean inEditMode = false;

    private TargetedCommand storedCommand;
    private Update lastUpdate;

    public enum Update {
        NORMAL_EXECUTION,
        INVALID_ARGUMENTS,
        STORE_COMMAND
    }

    public StubStateManager() {
        super(null, null, null, null);
    }

    @Override
    public void beforeCommandUpdate() {
        return;
    }


    @Override
    public Response updateAndStoreCommand(Result result, TargetedCommand command) {
        clearLastUpdate();
        storedCommand = command;
        lastUpdate = Update.STORE_COMMAND;

        return null;
    }

    @Override
    public Response update(Result result) {
        clearLastUpdate();
        if (result != null && result.getType() == Result.Type.INVALID_ARGUMENT) {
            lastUpdate = Update.INVALID_ARGUMENTS;
        } else {
            lastUpdate = Update.NORMAL_EXECUTION;
        }
        return null;
    }

    @Override
    public boolean canQuerySearchManager() {
        return true;
    }

    @Override
    public boolean canEdit() {
        return canEdit;
    }

    @Override
    public boolean inEditMode() {
        return inEditMode;
    }

    public Update getLastUpdate() {
        return lastUpdate;
    }

    public TargetedCommand getStoredCommand() {
        return storedCommand;
    }

    private void clearLastUpdate() {
        lastUpdate = null;
        storedCommand = null;
    }

}