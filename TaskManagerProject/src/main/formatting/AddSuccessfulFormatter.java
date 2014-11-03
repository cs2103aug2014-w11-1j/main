package main.formatting;

import main.message.AddSuccessfulMessage;

/**
 * Formatter for AddSuccessfulMessage.
 * @author nathanajah
 */
public class AddSuccessfulFormatter {
    private final static String FORMAT_LINE = "Task %1$s added successfully." +
            System.lineSeparator();
    
    /**
     * Formats an AddSuccessfulMessage to a String.
     * @param message The Message to be formatted.
     * @return The formatted Message.
     */
    public String format(AddSuccessfulMessage message) {
        return String.format(FORMAT_LINE, message.getTask().name);
    }
}
