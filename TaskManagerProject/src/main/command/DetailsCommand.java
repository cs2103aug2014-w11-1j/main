package main.command;

import manager.ManagerHolder;
import manager.datamanager.SearchManager;
import manager.result.Result;
import data.TaskId;

public class DetailsCommand extends TargetedCommand {
    private final SearchManager searchManager;

    private final TaskId taskId;

    public DetailsCommand(String args, ManagerHolder managerHolder) {
        super(managerHolder);
        searchManager = managerHolder.getSearchManager();

        taskId = parse(args);
    }

    private TaskId parse(String args) {
        // TODO use targetTaskIdSet for details command, alter searchManager
        String remaining = tryParseIdsIntoSet(args);
        if (remaining.length() > 0) {
            targetTaskIdSet = null;
        }
        if (targetTaskIdSet == null) {
            parseAsSearchString(args);
        }

        return parseTaskId(args);
    }

    @Override
    protected boolean isValidArguments() {
        return taskId != null;
    }

    @Override
    protected boolean isCommandAllowed() {
        return stateManager.canSearch();
    }

    @Override
    protected Result executeAction() {
        Result result = searchManager.details(taskId);
        return result;
    }

}
