package main.formatting;

import main.message.AliasMessage;
import main.message.AliasMessage.AliasType;

//@author A0065475X
public class AliasFormatter {

    public String format(AliasMessage aliasMessage) {
        String alias = aliasMessage.getAlias();
        String value = aliasMessage.getValue();
        boolean isReplacePrevious = aliasMessage.getIsReplacePrevious();
        AliasType messageType = aliasMessage.getMessageType();
        
        switch(messageType) {
            case ALIAS_DELETE_FAILURE :
                return deleteFailureFormat(alias);
            case ALIAS_DELETE_SUCCESS :
                return deleteSuccessFormat(alias, value);
            case ALIAS_SET_FAILURE :
                return setFailureFormat(alias);
            case ALIAS_SET_SUCCESS :
                return setSuccessFormat(alias, value, isReplacePrevious);
        }

        throw new UnsupportedOperationException("Unknown message type");
    }

    private String deleteFailureFormat(String alias) {
        return "Unable to unbind alias: " + alias + System.lineSeparator();
    }

    private String deleteSuccessFormat(String alias, String value) {
        return "Unbinded alias: " + alias + System.lineSeparator() +
                "Previously binded to: " + value + System.lineSeparator();
    }

    private String setFailureFormat(String alias) {
        return "Unable to set alias: " + alias + System.lineSeparator();
    }

    private String setSuccessFormat(String alias, String value,
            boolean isReplacePrevious) {
        
        String result = "The alias: " + alias + System.lineSeparator() +
                "has been binded to: " + value + System.lineSeparator();
        if (isReplacePrevious) {
            result += "Previous binding for " + alias +
                    " has been overwritten." + System.lineSeparator();
        }
        return result;
    }

}
