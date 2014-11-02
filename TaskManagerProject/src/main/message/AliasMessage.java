package main.message;

public class AliasMessage implements Message{

    private String alias;
    private String value;
    private AliasType type;
    
    public enum AliasType {
        ALIAS_SET_SUCCESS,
        ALIAS_SET_FAILURE,
        ALIAS_DELETE_SUCCESS,
        ALIAS_DELETE_FAILURE
    }
    
    public AliasMessage(String alias, String value, AliasType type) {
        this.alias = alias;
        this.value = value;
        this.type = type;
    }
    
    public String getAlias() {
        assert type == AliasType.ALIAS_SET_SUCCESS ||
                type == AliasType.ALIAS_DELETE_SUCCESS;
        return alias;
    }

    public String getValue() {
        assert type == AliasType.ALIAS_SET_SUCCESS;
        return value;
    }

    public AliasType getMessageType() {
        return type;
    }

    @Override
    public Type getType() {
        return Type.ALIAS_MESSAGE;
    }
}
