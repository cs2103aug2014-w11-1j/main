package main.formatting;

import main.response.EnumResponse;

public class EnumFormatter {
    
    private final static String MESSAGE_CANNOT_EXECUTE = 
            "Sorry, Taskline could not execute your command.";
    
    public String format(EnumResponse response) {
        String formattedResult;
        switch(response.getMessageType()) {
            case CANNOT_EXECUTE_COMMAND :
                formattedResult = MESSAGE_CANNOT_EXECUTE;
            default :
                formattedResult = "";
        }
        return formattedResult;
    }
}
