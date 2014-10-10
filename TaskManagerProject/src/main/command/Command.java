package main.command;
import main.response.Response;

public abstract class Command {
    
    public abstract Response execute();
}