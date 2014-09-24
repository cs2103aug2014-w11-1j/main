package main.response;

public class AddResponse implements Response{
	
	private final String OUTPUT_MESSAGE = "New Task Added.";

	@Override
	public Type getType() {
		return Type.MESSAGE;
	}
	

}
