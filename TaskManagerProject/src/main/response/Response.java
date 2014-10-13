package main.response;

import main.message.Message;
import main.modeinfo.ModeInfo;

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
}
