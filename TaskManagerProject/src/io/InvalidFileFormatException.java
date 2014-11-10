package io;

//@author A0065475X
public class InvalidFileFormatException extends Exception {
    private static final long serialVersionUID = -450599715167762851L;

    public InvalidFileFormatException(String message) {
        super(message);
    }
}
