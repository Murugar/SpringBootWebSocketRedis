package ccom.iqmsoft.redis.websocket.test.web.events;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.iqmsoft.redis.websocket.domain.model.Message;
import com.iqmsoft.redis.websocket.domain.model.Message.MessageBuilder;
import com.iqmsoft.redis.websocket.web.events.MyMessageBroadcaster;

import ccom.iqmsoft.redis.websocket.test.domain.model.MessageAssertions;

@RunWith(MockitoJUnitRunner.class)
public class ReceivedMessageBroadcasterTest {

    private MyMessageBroadcaster tested = new MyMessageBroadcaster();

    @Mock
    private SimpMessagingTemplate mockMessagingTemplate;

    @Before
    public void setup() {
        tested.setBrokerMessagingTemplate(mockMessagingTemplate);
    }

    @Test
    public void testNewMessageBroadcastsOnWebsocket() {

        Message testMessage = MessageBuilder.randomInfo().build();

        tested.handleMessage(testMessage, randomAlphabetic(20));

        // Asserts

        ArgumentCaptor<Message> sentPayload = ArgumentCaptor.forClass(Message.class);
        verify(mockMessagingTemplate).convertAndSend(Matchers.eq(MyMessageBroadcaster.WEBSOCKET_MESSAGE_TOPIC_PATH), sentPayload.capture());

        MessageAssertions.assertMessageEquals(testMessage, sentPayload.getValue());

    }

}
