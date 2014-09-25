package main.response;

public class EnumResponse implements Response{
    
    public enum MessageType {
        CANNOT_EXECUTE_COMMAND
    }
    
    MessageType messageType;
    
    public static EnumResponse cannotExecuteCommand() {
        return new EnumResponse(MessageType.CANNOT_EXECUTE_COMMAND);
    }
    
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
