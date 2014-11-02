package main.command;

import manager.ManagerHolder;
import manager.datamanager.AliasManager;
import manager.result.Result;

public class AliasDeleteCommand extends Command {

    private final AliasManager aliasManager;
    private String alias;
    
    public AliasDeleteCommand(String args, ManagerHolder managerHolder) {
        super(managerHolder);
        aliasManager = managerHolder.getAliasManager();
                
        parse(args);
    }
    
    private void parse(String args) {
        alias = args;
    }

    @Override
    protected boolean isValidArguments() {
        return (alias != null && alias.length() != 0);
    }

    @Override
    protected boolean isCommandAllowed() {
        return true;
    }

    @Override
    protected Result executeAction() {
        Result result = aliasManager.deleteAlias(alias);
        return result;
    }

}
