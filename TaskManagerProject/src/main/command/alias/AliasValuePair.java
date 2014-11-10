package main.command.alias;

/**
 * Contains an alias and the value it is mapped to.<br>
 * e.g. if "hp" is mapped to "edit \$ priority high", then we have:<br>
 * alias = "hp"<br>
 * value = "edit \$ priority high"<br>
 * <br>
 * Immutable
 */
//@author A0065475X
public class AliasValuePair {
    public final String alias;
    public final String value;
    
    public AliasValuePair(String alias, String value) {
        this.alias = alias;
        this.value = value;
    }
}
