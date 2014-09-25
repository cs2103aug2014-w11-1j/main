package main.formatting;

import java.util.ArrayList;

import main.response.SearchResponse;
import main.response.CannotExecuteCommandResponse;
import main.response.Response;
import data.taskinfo.TaskInfo;

public class Formatter {
    private final static String MESSAGE_CANNOT_EXECUTE_COMMAND =
            "Sorry, Taskline could not execute your command.";
    
    private ArrayList<String> formatCannotExecuteCommand(
            CannotExecuteCommandResponse response) {
        ArrayList<String> formattedArray = new ArrayList<String>();
        formattedArray.add(MESSAGE_CANNOT_EXECUTE_COMMAND);
        return formattedArray;
    }
    
    private String convertToString(ArrayList<String> lines) {
        StringBuilder resultBuilder = new StringBuilder("");
        for (String line : lines) {
            resultBuilder.append(line);
            resultBuilder.append(System.lineSeparator());
        }
        return resultBuilder.toString();
    }
    
    public String format(Response response) {
        ArrayList<String> lines;
        switch(response.getType()){
            case CANNOT_EXECUTE_COMMAND :
                CannotExecuteCommandResponse castedResponse = 
                    (CannotExecuteCommandResponse)response;
                lines = formatCannotExecuteCommand(castedResponse);
            default : 
                lines = new ArrayList<String>();
        }
        return convertToString(lines);
    }
}
