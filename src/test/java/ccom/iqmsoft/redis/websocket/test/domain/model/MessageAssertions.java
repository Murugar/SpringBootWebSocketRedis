package ccom.iqmsoft.redis.websocket.test.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.iqmsoft.redis.websocket.domain.model.Message;

public class MessageAssertions {

	
	private MessageAssertions()
	{
		
	}
	
    public static void assertMessageEquals(Message testMessage, Message captured) {
        assertEquals(testMessage.getContent(), captured.getContent());
        assertEquals(testMessage.getType(), captured.getType());
        assertNotNull(captured.getSender());
        assertNotNull(captured.getDate());
    }
    
}
