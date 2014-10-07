package main.formatting;

import main.message.AddSuccessfulMessage;
import main.message.DeleteSuccessfulMessage;
import main.message.DetailsMessage;
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
    private DetailsFormatter detailsFormatter;
    
    private EditModeFormatter editModeFormatter;
    private SearchModeFormatter searchModeFormatter;
    private EmptyModeFormatter emptyModeFormatter;
    
    class MessageModePair {
        public Message.Type messageType;
        public ModeInfo.Type modeInfoType;
        
        public MessageModePair(Message.Type messageType, 
                ModeInfo.Type modeInfoType) {
            this.messageType = messageType;
            this.modeInfoType = modeInfoType;
        }
    }
    
    private final MessageModePair modePrintPair[] = new MessageModePair[]{
            new MessageModePair(Message.Type.ADD_SUCCESSFUL, 
                    ModeInfo.Type.EDIT_MODE),
            new MessageModePair(Message.Type.ADD_SUCCESSFUL, 
                    ModeInfo.Type.SEARCH_MODE),
            new MessageModePair(Message.Type.DELETE_SUCCESSFUL, 
                    ModeInfo.Type.EDIT_MODE),
            new MessageModePair(Message.Type.DELETE_SUCCESSFUL, 
                    ModeInfo.Type.SEARCH_MODE),
            new MessageModePair(Message.Type.ENUM_MESSAGE,
                    ModeInfo.Type.SEARCH_MODE),
            new MessageModePair(Message.Type.ENUM_MESSAGE,
                    ModeInfo.Type.EDIT_MODE),
            new MessageModePair(Message.Type.EDIT_SUCCESSFUL,
                    ModeInfo.Type.SEARCH_MODE)
    };

    public Formatter() {
        addSuccessfulFormatter = new AddSuccessfulFormatter();
        deleteSuccessfulFormatter = new DeleteSuccessfulFormatter();
        editSuccessfulFormatter = new EditSuccessfulFormatter();
        enumFormatter = new EnumFormatter();
        detailsFormatter = new DetailsFormatter();
        
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
            case DETAILS :
                DetailsMessage detailsMessage = (DetailsMessage)message;
                formattedMessage = detailsFormatter.format(detailsMessage);
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
    
    private boolean shouldPrintMode(Response response) {
        for (MessageModePair pair : modePrintPair) {
            if (pair.messageType == response.getMessage().getType() && 
                    pair.modeInfoType == response.getModeInfo().getType()) {
                return true;
            }
        }
        return false;
    }
    
    public String format(Response response) {
        if (shouldPrintMode(response)) {
            return formatMessage(response.getMessage()) + 
                    formatModeInfo(response.getModeInfo());
        } else {
            return formatMessage(response.getMessage());
        }
    }
}
