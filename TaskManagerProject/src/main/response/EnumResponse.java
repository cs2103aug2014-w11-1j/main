package main.response;

public class EnumResponse implements Response{
    
    public enum MessageType {
        CANNOT_EXECUTE_COMMAND
    }
    
    MessageType messageType;
    
    public EnumResponse(MessageType messageType) {
        this.messageType = messageType;
    }
    
    public MessageType getMessageType() {
        return messageType;
    }
    
    public Type getType() {
        return Type.ENUM_MESSAGE;
    }
}
