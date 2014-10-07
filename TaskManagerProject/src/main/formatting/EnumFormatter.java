package main.formatting;

import main.message.EnumMessage;

public class EnumFormatter {
    
    private final static String MESSAGE_INVALID_ARGUMENT = 
            "Sorry, Taskline could not execute your command." +
            System.lineSeparator();
    private final static String MESSAGE_ADD_FAILED =
            "Add failed. Please try again." +
            System.lineSeparator();
    private final static String MESSAGE_EDIT_FAILED =
            "Edit failed. Please try again." +
            System.lineSeparator();
    private final static String MESSAGE_DELETE_FAILED =
            "Delete failed. Please try again." +
            System.lineSeparator();
    private final static String MESSAGE_EMPTY = "";
    private final static String MESSAGE_INVALID_COMMAND =
            "The command is invalid." + System.lineSeparator();
    
    public String format(EnumMessage message) {
        String formattedResult;
        switch(message.getMessageType()) {
            case INVALID_ARGUMENT :
                formattedResult = MESSAGE_INVALID_ARGUMENT;
                break;
            case INVALID_COMMAND :
                formattedResult = MESSAGE_INVALID_COMMAND;
                break;
            case EMPTY_STRING :
                formattedResult = MESSAGE_EMPTY;
                break;
            case ADD_FAILED :
                formattedResult = MESSAGE_ADD_FAILED;
                break;
            case EDIT_FAILED :
                formattedResult = MESSAGE_EDIT_FAILED;
                break;
            case DELETE_FAILED :
                formattedResult = MESSAGE_DELETE_FAILED;
                break;
            default :
                formattedResult = "";
                break;
        }
        return formattedResult;
    }
}
