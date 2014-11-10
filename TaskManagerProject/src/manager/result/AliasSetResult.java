package manager.result;


//@author A0065475X
public class AliasSetResult implements Result {

    private final String alias;
    private final String value;
    private final boolean replacePrevious;
    private final Type type;
    
    public AliasSetResult(String alias, String value, boolean replacePrevious,
            Type type) {
        this.alias = alias;
        this.value = value;
        this.replacePrevious = replacePrevious;
        
        assert type == Type.ALIAS_SUCCESS ||
                type == Type.ALIAS_FAILURE;
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
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
