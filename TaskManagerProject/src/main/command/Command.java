package main.command;
import main.response.Response;

public interface Command {
    
    public Response execute();
}