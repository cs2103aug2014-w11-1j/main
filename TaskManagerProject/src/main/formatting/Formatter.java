package main.formatting;

import java.util.ArrayList;

import main.response.SearchResponse;
import main.response.EnumResponse;
import main.response.Response;
import data.taskinfo.TaskInfo;

public class Formatter {
    EnumFormatter enumFormatter;
    SearchFormatter searchFormatter;
    
    public Formatter() {
        enumFormatter = new EnumFormatter();
        searchFormatter = new SearchFormatter();
    }
    
    public String format(Response response) {
        String formattedResponse = "";
        switch(response.getType()) {
            case ENUM_MESSAGE :
                EnumResponse enumResponse = (EnumResponse)response;
                formattedResponse = enumFormatter.format(enumResponse);
                break;
            case SEARCH_RESULTS :
                SearchResponse searchResponse = (SearchResponse)response;
                formattedResponse = searchFormatter.format(searchResponse);
            default : 
                //TODO: implement other responses
        }
        return formattedResponse;
    }
}
