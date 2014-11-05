package main.command;

import manager.ManagerHolder;
import manager.datamanager.AliasManager;
import manager.result.Result;

public class ViewAliasCommand extends Command {
    private final AliasManager aliasManager;

    public ViewAliasCommand(ManagerHolder managerHolder) {
        super(managerHolder);
        this.aliasManager = managerHolder.getAliasManager();
    }

    @Override
    protected boolean isValidArguments() {
        return true;
    }

    @Override
    protected boolean isCommandAllowed() {
        return true;
    }

    @Override
    protected Result executeAction() {
        Result result = aliasManager.viewAliases();
        return result;
    }

}
