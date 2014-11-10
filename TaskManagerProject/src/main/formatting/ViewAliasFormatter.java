package main.formatting;

import main.command.alias.AliasValuePair;
import main.message.ViewAliasMessage;

//@author A0065475X
/**
 * Formatter for ViewAliasMessage. (when the user uses the view alias command)
 */
public class ViewAliasFormatter {
    private static final String ALIAS_HEADER = "Aliases currently binded:" +
            System.lineSeparator();
    private static final String ALIAS_FORMAT = "[%1$d]: %2$s --> %3$s";

    public String format(ViewAliasMessage aliasMessage) {
        AliasValuePair[] aliases = aliasMessage.getAliases();
        
        StringBuilder sb = new StringBuilder();
        
        sb.append(ALIAS_HEADER);
        
        int index = 1;
        for (AliasValuePair alias : aliases) {
            sb.append(formatAlias(index, alias));
            sb.append(System.lineSeparator());
            
            index++;
        }
        
        return sb.toString();
    }

    private String formatAlias(int index, AliasValuePair aliasValuePair) {
        return String.format(ALIAS_FORMAT, index, aliasValuePair.alias,
                aliasValuePair.value);
    }

}
