package manager.datamanager;

import main.command.alias.IAliasStorage;
import manager.result.AliasDeleteResult;
import manager.result.AliasSetResult;
import manager.result.Result;
import manager.result.SimpleResult;
import data.TaskData;

public class AliasManager extends AbstractManager {
    private final IAliasStorage aliasStorage;

    public AliasManager(IAliasStorage aliasStorage, TaskData taskData) {
        super(taskData);
        this.aliasStorage = aliasStorage;
    }
    
    public Result createAlias(String alias, String target) {
        boolean canOverride = aliasStorage.canOverride(alias);
        if (canOverride) {
            String value;
            boolean alreadyHasAlias = aliasStorage.isAlreadyBinded(alias);
            value = aliasStorage.createCustomCommand(alias, target);
            
            return new AliasSetResult(alias, value, alreadyHasAlias);
            
        } else {
            return new SimpleResult(Result.Type.ALIAS_FAILURE);
        }
    }

    public Result deleteAlias(String alias) {
        String value = aliasStorage.deleteCustomCommand(alias);
        if (value != null) {
            return new AliasDeleteResult(alias, value);
        }
        else {
            return new SimpleResult(Result.Type.ALIAS_DELETE_FAILURE);
        }
    }
}
