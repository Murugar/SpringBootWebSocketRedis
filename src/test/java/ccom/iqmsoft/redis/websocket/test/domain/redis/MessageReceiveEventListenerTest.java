package ccom.iqmsoft.redis.websocket.test.domain.redis;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.iqmsoft.redis.websocket.domain.model.Message;
import com.iqmsoft.redis.websocket.domain.model.Message.MessageBuilder;
import com.iqmsoft.redis.websocket.domain.redis.MyMessageEventListener;
import com.iqmsoft.redis.websocket.domain.service.MessageService;

import ccom.iqmsoft.redis.websocket.test.domain.model.MessageAssertions;


@RunWith(MockitoJUnitRunner.class)
public class MessageReceiveEventListenerTest {

    @Mock
    private MessageService mockMessageService;

    private MyMessageEventListener tested = new MyMessageEventListener();

    @Before
    public void setupMocks() {
        tested.setMessageService(mockMessageService);
    }

    @Test
    public void testHandleMessage() {

        Message testMessage = MessageBuilder.randomInfo().build();
        tested.handleMessage(testMessage, randomAlphabetic(20));

        ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(mockMessageService).create(messageCaptor.capture());

        MessageAssertions.assertMessageEquals(testMessage, messageCaptor.getValue());
    }
}
