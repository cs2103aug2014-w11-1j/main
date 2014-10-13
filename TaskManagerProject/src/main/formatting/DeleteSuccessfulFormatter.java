package main.formatting;

import main.message.DeleteSuccessfulMessage;

/**
 * Formatter for DeleteSuccessfulMessage.
 * @author nathanajah
 */
public class DeleteSuccessfulFormatter {
    private final static String FORMAT_LINE = "Task %1$s deleted." +
            System.lineSeparator();
    
    /**
     * Formats a DeleteSuccessfulMessage to a String.
     * @param message The DeleteSuccessfulMessage to be formatted.
     * @return The formatted Message.
     */
    public String format(DeleteSuccessfulMessage message) {
        return String.format(FORMAT_LINE, message.getTask().name);
    }
}
