package main.formatting;

import main.message.AddSuccessfulMessage;
import main.message.DeleteSuccessfulMessage;
import main.message.EditSuccessfulMessage;
import main.message.EnumMessage;
import main.message.Message;
import main.modeinfo.EditModeInfo;
import main.modeinfo.EmptyModeInfo;
import main.modeinfo.ModeInfo;
import main.modeinfo.SearchModeInfo;
import main.response.Response;


public class Formatter {
    private AddSuccessfulFormatter addSuccessfulFormatter;
    private DeleteSuccessfulFormatter deleteSuccessfulFormatter;
    private EditSuccessfulFormatter editSuccessfulFormatter;
    private EnumFormatter enumFormatter;
    
    private EditModeFormatter editModeFormatter;
    private SearchModeFormatter searchModeFormatter;
    private EmptyModeFormatter emptyModeFormatter;

    public Formatter() {
        addSuccessfulFormatter = new AddSuccessfulFormatter();
        deleteSuccessfulFormatter = new DeleteSuccessfulFormatter();
        editSuccessfulFormatter = new EditSuccessfulFormatter();
        enumFormatter = new EnumFormatter();
        
        editModeFormatter = new EditModeFormatter();
        searchModeFormatter = new SearchModeFormatter();
        emptyModeFormatter = new EmptyModeFormatter();
    }
    
    private String formatMessage(Message message) {
        String formattedMessage = "";
        switch(message.getType()) {
            case ADD_SUCCESSFUL :
                AddSuccessfulMessage addSuccessfulMessage = 
                        (AddSuccessfulMessage)message;
                formattedMessage = addSuccessfulFormatter.format(
                        addSuccessfulMessage);
                break;
            case EDIT_SUCCESSFUL :
                EditSuccessfulMessage editSuccessfulMessage =
                        (EditSuccessfulMessage)message;
                formattedMessage = editSuccessfulFormatter.format(
                        editSuccessfulMessage);
                break;
            case DELETE_SUCCESSFUL :
                DeleteSuccessfulMessage deleteSuccessfulMessage =
                        (DeleteSuccessfulMessage)message;
                formattedMessage = deleteSuccessfulFormatter.format(
                        deleteSuccessfulMessage);
                break;
            case ENUM_MESSAGE :
                EnumMessage enumMessage = (EnumMessage)message;
                formattedMessage = enumFormatter.format(enumMessage);
                break;
        }
        return formattedMessage;
    }
    
    private String formatModeInfo(ModeInfo modeInfo) {
        String formattedModeInfo = "";
        switch (modeInfo.getType()) {
            case EMPTY_MODE :
                EmptyModeInfo emptyModeInfo = (EmptyModeInfo) modeInfo;
                formattedModeInfo = emptyModeFormatter.format(emptyModeInfo);
                break;
            case EDIT_MODE :
                EditModeInfo editModeInfo = (EditModeInfo) modeInfo;
                formattedModeInfo = editModeFormatter.format(editModeInfo);
                break;
            case SEARCH_MODE :
                SearchModeInfo searchModeInfo = (SearchModeInfo) modeInfo;
                formattedModeInfo = searchModeFormatter.format(searchModeInfo);
                break;
        }
        return formattedModeInfo;
    }
    
    public String format(Response response) {
        return formatMessage(response.getMessage()) + 
                formatModeInfo(response.getModeInfo());
    }
}
