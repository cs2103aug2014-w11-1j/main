package main.message;

/**
 * For any message that does not need to contain any more information than its
 * type.
 */
public class EnumMessage implements Message{
    public enum MessageType {
        EMPTY_STRING,
        INVALID_ARGUMENT,
        INVALID_COMMAND,
        ADD_FAILED,
        DELETE_FAILED,
        EDIT_FAILED,
        ADD_TAG_FAILED,
        DELETE_TAG_FAILED,
        SEARCH_SUCCESS,
        SEARCH_FAILED,
        EDIT_STARTED,
        EDIT_ENDED,
        UNDO_SUCCESS,
        UNDO_FAILED,
        REDO_SUCCESS,
        REDO_FAILED,
        SEARCH_ENDED,
        EXIT
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
        return new EnumMessage(MessageType.INVALID_ARGUMENT);
    }
}
