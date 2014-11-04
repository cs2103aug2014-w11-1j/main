package manager.result;

//@author A0065475X
public class AliasSetResult implements Result {

    private final String alias;
    private final String value;
    private final boolean replacePrevious;
    
    public AliasSetResult(String alias, String value, boolean replacePrevious) {
        this.alias = alias;
        this.value = value;
        this.replacePrevious = replacePrevious;
    }

    @Override
    public Type getType() {
        return Type.ALIAS_SUCCESS;
    }
    
    public String getAlias() {
        return alias;
    }

    public String getValue() {
        return value;
    }

    public boolean isReplacePrevious() {
        return replacePrevious;
    }

}
