package main.formatting;

import main.message.AddSuccessfulMessage;
import main.message.DeleteSuccessfulMessage;
import main.message.DetailsMessage;
import main.message.EditSuccessfulMessage;
import main.message.EnumMessage;
import main.message.FreeDaySearchMessage;
import main.message.Message;
import main.message.ReportMessage;
import main.modeinfo.EditModeInfo;
import main.modeinfo.EmptyModeInfo;
import main.modeinfo.ModeInfo;
import main.modeinfo.SearchModeInfo;
import main.response.Response;

/**
 * Formatter. Responsible for formatting Response objects to String.
 */

//@author A0113011L
public class Formatter {
    private AddSuccessfulFormatter addSuccessfulFormatter;
    private DeleteSuccessfulFormatter deleteSuccessfulFormatter;
    private EditSuccessfulFormatter editSuccessfulFormatter;
    private EnumFormatter enumFormatter;
    private DetailsFormatter detailsFormatter;
    private ReportFormatter reportFormatter;
    private FreeDaySearchFormatter freeDaySearchFormatter;
    
    private EditModeFormatter editModeFormatter;
    private SearchModeFormatter searchModeFormatter;
    private EmptyModeFormatter emptyModeFormatter;
    private WaitingModeFormatter waitingModeFormatter;
    
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
                    ModeInfo.Type.SEARCH_MODE),
            new MessageModePair(Message.Type.ENUM_MESSAGE,
                    ModeInfo.Type.WAITING_MODE),
    };

    /**
     * The constructor for Formatter.
     */
    public Formatter() {
        addSuccessfulFormatter = new AddSuccessfulFormatter();
        deleteSuccessfulFormatter = new DeleteSuccessfulFormatter();
        editSuccessfulFormatter = new EditSuccessfulFormatter();
        enumFormatter = new EnumFormatter();
        detailsFormatter = new DetailsFormatter();
        reportFormatter = new ReportFormatter();
        freeDaySearchFormatter = new FreeDaySearchFormatter();
        
        editModeFormatter = new EditModeFormatter();
        searchModeFormatter = new SearchModeFormatter();
        emptyModeFormatter = new EmptyModeFormatter();
        waitingModeFormatter = new WaitingModeFormatter();
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
                break;
            case REPORT :
                ReportMessage reportMessage = (ReportMessage)message;
                formattedMessage = reportFormatter.format(reportMessage);
                break;
            case FREE_DAY_SEARCH_SUCCESSFUL :
                FreeDaySearchMessage freeDaySearchMessage = 
                        (FreeDaySearchMessage)message;
                formattedMessage = 
                        freeDaySearchFormatter.format(freeDaySearchMessage);

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
                SearchModeInfo waitingModeInfo = (SearchModeInfo) modeInfo;
                formattedModeInfo = searchModeFormatter.format(waitingModeInfo);
                break;
            case WAITING_MODE :
                SearchModeInfo searchModeInfo = (SearchModeInfo) modeInfo;
                formattedModeInfo = waitingModeFormatter.format(searchModeInfo);
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
    
    /**
     * Format the Response to String.
     * @param response The Response object to be formatted.
     * @return The formatted String.
     */
    public String format(Response response) {
        if (shouldPrintMode(response)) {
            return formatMessage(response.getMessage()) +
                    formatModeInfo(response.getModeInfo());
        } else {
            return formatMessage(response.getMessage());
        }
    }
}
