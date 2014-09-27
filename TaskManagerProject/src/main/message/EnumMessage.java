package main.message;

public class EnumMessage implements Message{
    public enum MessageType {
        EMPTY_STRING,
        CANNOT_EXECUTE_COMMAND
    }
    
    MessageType messageType;
    
    public Type getType() {
        return Type.ENUM_MESSAGE;
    }
    
    public EnumMessage(MessageType messageType) {
        this.messageType = messageType;
    }
    
    public MessageType getMessageType() {
        return messageType;
    }
    
    public static EnumMessage emptyString() {
        return new EnumMessage(MessageType.EMPTY_STRING);
    }
    
    public static EnumMessage cannotExecuteCommand() {
        return new EnumMessage(MessageType.CANNOT_EXECUTE_COMMAND);
    }
}
