package main.response;

public interface Response {

    public enum Type {
        EMPTY_STRING,   // print no output
        MESSAGE,        // print only displayed message
        ENUM_MESSAGE,    // print the message type stored in the enum.
        SEARCH_RESULTS,
        EDITMODE,
    }
    
    public Type getType();
}
