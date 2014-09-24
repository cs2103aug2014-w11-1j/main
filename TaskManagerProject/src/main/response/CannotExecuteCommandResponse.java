package main.response;

public class CannotExecuteCommandResponse implements Response{

    @Override
    public Type getType() {
        return Type.CANNOT_EXECUTE_COMMAND;
    }

}
