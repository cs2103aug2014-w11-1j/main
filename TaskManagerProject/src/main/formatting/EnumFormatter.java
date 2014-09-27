package main.formatting;

import main.message.EnumMessage;

public class EnumFormatter {
    
    private final static String MESSAGE_CANNOT_EXECUTE = 
            "Sorry, Taskline could not execute your command.";
    
    public String format(EnumMessage message) {
        String formattedResult;
        switch(message.getMessageType()) {
            case CANNOT_EXECUTE_COMMAND :
                formattedResult = MESSAGE_CANNOT_EXECUTE;
            case EMPTY_STRING :
                formattedResult = "";
            default :
                formattedResult = "";
        }
        return formattedResult;
    }
}
