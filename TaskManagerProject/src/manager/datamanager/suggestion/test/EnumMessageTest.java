package manager.datamanager.suggestion.test;

import main.formatting.EnumFormatter;
import main.message.EnumMessage;
import main.message.EnumMessage.MessageType;

import org.junit.Test;

public class EnumMessageTest {

	@Test
	public void testInvalidArgument() {
		EnumMessage invalidArgumentMessage = 
				new EnumMessage(MessageType.INVALID_ARGUMENT);
		EnumFormatter formatter = new EnumFormatter();
		
		
	}

}
