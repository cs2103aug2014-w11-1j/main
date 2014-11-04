package main.message;

//@author A0065475X
public class AliasMessage implements Message {

    private String alias;
    private String value;
    private boolean isReplacePrevious;
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
    
    public AliasMessage(String alias, String value, boolean isReplacePrevious,
            AliasType type) {
        this.alias = alias;
        this.value = value;
        this.type = type;
        this.isReplacePrevious = isReplacePrevious;
    }
    
    /**
     * Note: used for all message types.
     * @return the alias keyword
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Note: only used for ALIAS_SET_SUCCESS, ALIAS_SET_FAILURE and
     * ALIAS_DELETE_SUCCESS
     * @return 
     * ALIAS_SET_SUCCESS: the string it is binded to
     * ALIAS_DELETE_SUCCESS: return the string it was previously binded to.
     * ALIAS_SET_FAILURE: return the value string the player tried to type.
     */
    public String getValue() {
        return value;
    }

    /**
     * Note: only used for ALIAS_SET_SUCCESS
     * @return true iff the new alias overwrites an old one.
     */
    public boolean getIsReplacePrevious() {
        return isReplacePrevious;
    }

    public AliasType getMessageType() {
        return type;
    }

    @Override
    public Type getType() {
        return Type.ALIAS_MESSAGE;
    }
}
