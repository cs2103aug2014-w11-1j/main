package main.formatting;

import main.message.AddSuccessfulMessage;

public class AddSuccessfulFormatter {
    private final static String FORMAT_LINE = "Task %1$s added sucessfully." +
            System.lineSeparator();
    
    public String format(AddSuccessfulMessage message) {
        return String.format(FORMAT_LINE, message.getTask().name);
    }
}
