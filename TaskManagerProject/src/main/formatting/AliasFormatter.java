package main.formatting;

import main.message.AliasMessage;
import main.message.AliasMessage.AliasType;

//@author A0065475X
/**
 * Formatter for Alias Messages (success/failure, bind/unbind).
 */
public class AliasFormatter {
    private static final String MESSAGE_DELETE_FAIL =
            "Unable to unbind alias: %1$s" + System.lineSeparator() + 
            "Alias does not exist." + System.lineSeparator();
    private static final String MESSAGE_DELETE_SUCCESS =
            "Unbinded alias: %1$s" + System.lineSeparator() +
            "Previously binded to: %2$s" + System.lineSeparator();
    private static final String MESSAGE_SET_FAIL =
            "Unable to set alias: %1$s" + System.lineSeparator() +
            "The keyword is reserved." + System.lineSeparator();
    private static final String MESSAGE_SET_SUCCESS =
            "The alias: %1$s" + System.lineSeparator() +
            "has been binded to: %2$s" + System.lineSeparator();
    private static final String MESSAGE_SET_SUCCESS_OVERWRITE =
            "Previous binding for %1$s has been overwritten." +
            System.lineSeparator();

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
        return String.format(MESSAGE_DELETE_FAIL, alias);
    }

    private String deleteSuccessFormat(String alias, String value) {
        return String.format(MESSAGE_DELETE_SUCCESS, alias, value);
    }

    private String setFailureFormat(String alias) {
        return String.format(MESSAGE_SET_FAIL, alias);
    }

    private String setSuccessFormat(String alias, String value,
            boolean isReplacePrevious) {
        
        String result = String.format(MESSAGE_SET_SUCCESS, alias, value);
        if (isReplacePrevious) {
            result += String.format(MESSAGE_SET_SUCCESS_OVERWRITE, alias);
        }

        return result;
    }

}
