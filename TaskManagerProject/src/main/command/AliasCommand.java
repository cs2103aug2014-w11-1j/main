package main.command;

import manager.ManagerHolder;
import manager.datamanager.AliasManager;
import manager.result.Result;

public class AliasCommand extends Command {

    private final AliasManager aliasManager;
    private String alias;
    private String value;
    
    public AliasCommand(String args, ManagerHolder managerHolder) {
        super(managerHolder);
        aliasManager = managerHolder.getAliasManager();
                
        parse(args);
    }
    
    private void parse(String args) {
        String[] tokens = args.split(" ", 2);
        if (tokens.length < 2) {
            alias = null;
            return;
        } else {
            alias = tokens[0];
            value = tokens[1];
        }
    }

    @Override
    protected boolean isValidArguments() {
        return (alias != null);
    }

    @Override
    protected boolean isCommandAllowed() {
        return true;
    }

    @Override
    protected Result executeAction() {
        Result result = aliasManager.createAlias(alias, value);
        return result;
    }

}
