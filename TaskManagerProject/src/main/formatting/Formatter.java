package main.formatting;

import java.util.ArrayList;

import main.response.SearchResponse;
import main.response.EnumResponse;
import main.response.Response;
import data.taskinfo.TaskInfo;

public class Formatter {
    EnumFormatter enumFormatter;
    
    public Formatter() {
        enumFormatter = new EnumFormatter();
    }
    
    public String format(Response response) {
        String formattedResponse = "";
        switch(response.getType()) {
            case ENUM_MESSAGE :
                EnumResponse castedResponse = (EnumResponse)response;
                formattedResponse = enumFormatter.format(castedResponse);
            default : 
                //TODO: implement other responses
        }
        return formattedResponse;
    }
}
