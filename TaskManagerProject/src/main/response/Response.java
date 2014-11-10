package main.response;

import main.message.EnumMessage;
import main.message.Message;
import main.modeinfo.ModeInfo;

//@author A0113011L
/**
 * Carries a Message and a ModeInfo.<br>
 * Message carries information about the last command execution.<br>
 * ModeInfo carries information about the current state the program is in.
 */
public class Response {

    Message message;
    ModeInfo modeInfo;
    
    /**
     * The constructor for the Response class.
     * @param message The Message part of this Response.
     * @param modeInfo The ModeInfo part of this Response.
     */
    public Response(Message message, ModeInfo modeInfo) {
        this.message = message;
        this.modeInfo = modeInfo;
    }
    
    /**
     * Get the Message part of the Response.
     * @return Message part of the Response.
     */
    public Message getMessage() {
        return message;
    }
    
    /**
     * Get the ModeInfo part of the Response.
     * @return ModeInfo part of the Response.
     */
    public ModeInfo getModeInfo() {
        return modeInfo;
    }
    
    public boolean isExitResponse() {
        if (message == null) {
            return false;
            
        } else if (message.getType() != Message.Type.ENUM_MESSAGE) {
            return false;
            
        } else {
            EnumMessage enumMessage = (EnumMessage)message;
            return (enumMessage.getMessageType() == EnumMessage.MessageType.EXIT);
        }
    }
}
