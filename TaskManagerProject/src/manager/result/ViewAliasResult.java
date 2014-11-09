package manager.result;

import java.util.Arrays;

import main.command.alias.AliasValuePair;

public class ViewAliasResult implements Result {
    private final AliasValuePair[] aliasValuePairs;
    
    public ViewAliasResult(AliasValuePair[] aliasValuePairs) {
        this.aliasValuePairs = Arrays.copyOf(aliasValuePairs,
                aliasValuePairs.length);
    }
    
    public AliasValuePair[] getAliases() {
        return aliasValuePairs;
    }

    @Override
    public Type getType() {
        return Type.VIEW_ALIAS_SUCCESS;
    }
}
