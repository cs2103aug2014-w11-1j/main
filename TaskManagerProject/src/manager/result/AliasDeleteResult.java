package manager.result;

//@author A0065475X
public class AliasDeleteResult implements Result{

    private final String alias;
    private final String value;
    
    public AliasDeleteResult(String alias, String value) {
        this.alias = alias;
        this.value = value;
    }
    
    public String getAlias() {
        return alias;
    }
    
    public String getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return Result.Type.ALIAS_DELETE_SUCCESS;
    }
}
