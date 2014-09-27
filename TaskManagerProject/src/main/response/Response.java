package main.response;

import main.message.Message;
import main.modeinfo.ModeInfo;

public class Response {

    Message message;
    ModeInfo modeInfo;
    
    public Response(Message message, ModeInfo modeInfo) {
        this.message = message;
        this.modeInfo = modeInfo;
    }
    
    public Message getMessage() {
        return message;
    }
    
    public ModeInfo getModeInfo() {
        return modeInfo;
    }
}
