package manager.datamanager;

import main.command.alias.AliasValuePair;
import main.command.alias.IAliasStorage;
import manager.result.AliasDeleteResult;
import manager.result.AliasSetResult;
import manager.result.Result;
import manager.result.ViewAliasResult;
import data.TaskData;

//@author A0065475X
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
            boolean isAlreadyBinded = aliasStorage.isAlreadyBinded(alias);
            value = aliasStorage.createCustomCommand(alias, target);
            
            return new AliasSetResult(alias, value, isAlreadyBinded,
                    Result.Type.ALIAS_SUCCESS);
            
        } else {
            return new AliasSetResult(alias, target, false,
                    Result.Type.ALIAS_FAILURE);
        }
    }

    public Result deleteAlias(String alias) {
        String value = aliasStorage.deleteCustomCommand(alias);
        if (value != null) {
            return new AliasDeleteResult(alias, value,
                    Result.Type.ALIAS_DELETE_SUCCESS);
        }
        else {
            return new AliasDeleteResult(alias, null,
                    Result.Type.ALIAS_DELETE_FAILURE);
        }
    }
    
    public Result viewAliases() {
        AliasValuePair[] aliasValuePairs = aliasStorage.getAllCustomAliases();
        return new ViewAliasResult(aliasValuePairs);
    }
}
