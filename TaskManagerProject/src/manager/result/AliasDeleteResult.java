package manager.result;

//@author A0065475X
public class AliasDeleteResult implements Result {

    private final String alias;
    private final String value;
    private final Type type;
    
    public AliasDeleteResult(String alias, String value, Type type) {
        this.alias = alias;
        this.value = value;
        
        assert type == Type.ALIAS_DELETE_SUCCESS ||
                type == Type.ALIAS_DELETE_FAILURE;
        this.type = type;
    }
    
    public String getAlias() {
        return alias;
    }
    
    public String getValue() {
        return value;
    }

    @Override
    public Type getType() {
        return type;
    }
}
