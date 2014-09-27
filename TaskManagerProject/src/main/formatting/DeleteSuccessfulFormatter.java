package main.formatting;

import main.message.DeleteSuccessfulMessage;

public class DeleteSuccessfulFormatter {
    private final static String FORMAT_LINE = "Task %1$s deleted." +
            System.lineSeparator();
    
    public String format(DeleteSuccessfulMessage message) {
        return String.format(FORMAT_LINE, message.getTask().name);
    }
}
