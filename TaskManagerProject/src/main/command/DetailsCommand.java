package main.command;

import manager.ManagerHolder;
import manager.datamanager.SearchManager;
import manager.result.Result;

public class DetailsCommand extends TargetedCommand {
    private final SearchManager searchManager;

    public DetailsCommand(String args, ManagerHolder managerHolder) {
        super(managerHolder);
        searchManager = managerHolder.getSearchManager();

        parse(args);
    }

    private void parse(String args) {
        String remaining = tryParseIdsIntoSet(args);
        if (remaining.length() > 0) {
            targetTaskIdSet = null;
        }
        if (targetTaskIdSet == null) {
            parseAsSearchString(args);
        }
    }

    @Override
    protected boolean isValidArguments() {
        return targetTaskIdSet != null;
    }

    @Override
    protected boolean isCommandAllowed() {
        return stateManager.canSearch();
    }

    @Override
    protected Result executeAction() {
        Result result = searchManager.details(targetTaskIdSet);
        return result;
    }

}
