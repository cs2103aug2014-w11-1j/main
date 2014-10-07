package main.formatting;

import main.message.EnumMessage;

public class EnumFormatter {
    
    private final static String MESSAGE_INVALID_ARGUMENT = 
            "Sorry, Taskline could not execute your command." +
            System.lineSeparator();    
    private final static String MESSAGE_INVALID_COMMAND =
            "The command is invalid." + System.lineSeparator();            
    private final static String MESSAGE_ADD_FAILED =
            "Add failed. Please try again." +
            System.lineSeparator();
    private final static String MESSAGE_DELETE_FAILED =
            "Delete failed. Please try again." +
            System.lineSeparator();
    private final static String MESSAGE_EDIT_FAILED =
            "Edit failed. Please try again." +
            System.lineSeparator();
    private final static String MESSAGE_ADD_TAG_FAILED =
            "Add tag failed. Please try again." +
            System.lineSeparator();
    private final static String MESSAGE_DELETE_TAG_FAILED =
            "Delete tag failed. Please try again." +
            System.lineSeparator();
    private final static String MESSAGE_SEARCH_SUCCESS = "";
    private final static String MESSAGE_SEARCH_FAILED = 
            "Search failed. Please try again." +
            System.lineSeparator();
    private final static String MESSAGE_EDIT_STARTED = "";
    private final static String MESSAGE_EDIT_ENDED = 
            "Exiting edit mode." + 
            System.lineSeparator();
    private final static String MESSAGE_UNDO_SUCCESS = 
            "Undo successful." + 
            System.lineSeparator();
    private final static String MESSAGE_UNDO_FAILED = 
            "Undo failed. Please try again." +
            System.lineSeparator();
    private final static String MESSAGE_REDO_SUCCESS =
            "Redo successful." +
            System.lineSeparator();
    private final static String MESSAGE_REDO_FAILED =
            "Redo failed." +
            System.lineSeparator();
    private final static String MESSAGE_SEARCH_ENDED =
            "Search ended." +
            System.lineSeparator();
    private final static String MESSAGE_EMPTY = "";

    public String format(EnumMessage message) {
        String formattedResult;
        switch(message.getMessageType()) {
            case EMPTY_STRING :
                formattedResult = MESSAGE_EMPTY;
                break;
            case INVALID_ARGUMENT :
                formattedResult = MESSAGE_INVALID_ARGUMENT;
                break;
            case INVALID_COMMAND :
                formattedResult = MESSAGE_INVALID_COMMAND;
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
            case ADD_TAG_FAILED :
                formattedResult = MESSAGE_ADD_TAG_FAILED;
                break;
            case DELETE_TAG_FAILED :
                formattedResult = MESSAGE_DELETE_TAG_FAILED;
                break;
            case SEARCH_SUCCESS :
                formattedResult = MESSAGE_SEARCH_SUCCESS;
                break;
            case SEARCH_FAILED :
                formattedResult = MESSAGE_SEARCH_FAILED;
                break;
            case EDIT_STARTED :
                formattedResult = MESSAGE_EDIT_STARTED;
                break;
            case EDIT_ENDED :
                formattedResult = MESSAGE_EDIT_ENDED;
                break;
            case UNDO_SUCCESS :
                formattedResult = MESSAGE_UNDO_SUCCESS;
                break;
            case UNDO_FAILED :
                formattedResult = MESSAGE_UNDO_FAILED;
                break;
            case REDO_SUCCESS :
                formattedResult = MESSAGE_REDO_SUCCESS;
                break;
            case REDO_FAILED :
                formattedResult = MESSAGE_REDO_FAILED;
                break;
            case SEARCH_ENDED :
                formattedResult = MESSAGE_SEARCH_ENDED;
                break;
            default :
                formattedResult = "";
                break;
        }
        return formattedResult;
    }
}
