package main.formatting;

import main.message.DeleteSuccessfulMessage;

/**
 * Formatter for DeleteSuccessfulMessage.
 */

//@author A0113011L
public class DeleteSuccessfulFormatter {
    private final static String FORMAT_SINGLE = "Task %1$s deleted." +
            System.lineSeparator();
    private final static String HEADER_MULTI = "%1$d tasks deleted.";
    private final static String TASKNAME_MULTI = "- %1$s";
    
    /**
     * Formats a DeleteSuccessfulMessage to a String.
     * @param message The DeleteSuccessfulMessage to be formatted.
     * @return The formatted Message.
     */
    public String format(DeleteSuccessfulMessage message) {
        assert message.getTask().length > 0;
        
        switch(message.getTask().length) {
            case 1 :
                return formatSingleTask(message);
            default :
                return formatMultiTask(message);
        }
    }
    
    private String formatSingleTask(DeleteSuccessfulMessage message) {
        assert message.getTask().length == 1;
        return String.format(FORMAT_SINGLE, message.getTask()[0].name) + System.lineSeparator();
    }
    
    private String formatMultiTask(DeleteSuccessfulMessage message) {
        assert message.getTask().length > 1;
        return getMultiHeader(message) + getMultiTaskList(message) + System.lineSeparator();
    }
    
    private String getMultiHeader(DeleteSuccessfulMessage message) {
        return String.format(HEADER_MULTI, message.getTask().length) + 
                System.lineSeparator();
    }
    
    private String getMultiTaskList(DeleteSuccessfulMessage message) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < message.getTask().length; i++) {
            builder.append(String.format(TASKNAME_MULTI, 
                    message.getTask()[i].name));
            builder.append(System.lineSeparator());
        }
        
        return builder.toString();
    }
}
