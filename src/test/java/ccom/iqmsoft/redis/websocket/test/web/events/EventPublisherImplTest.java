package ccom.iqmsoft.redis.websocket.test.web.events;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.redis.core.RedisTemplate;

import com.iqmsoft.redis.websocket.domain.model.Message;
import com.iqmsoft.redis.websocket.domain.model.Message.MessageBuilder;
import com.iqmsoft.redis.websocket.domain.redis.MyMessageEventListener;
import com.iqmsoft.redis.websocket.web.events.MessageEventPublisherImpl;

import ccom.iqmsoft.redis.websocket.test.domain.model.MessageAssertions;


@RunWith(MockitoJUnitRunner.class)
public class EventPublisherImplTest {

    @Mock
    private RedisTemplate<String, Message> mockRedisTemplate;

    private MessageEventPublisherImpl tested = new MessageEventPublisherImpl();

    @Before
    public void setup() {
        tested.setMessageRedisTemplate(mockRedisTemplate);
    }

    @Test
    public void testPublishReceive() {

        Message testMessage = MessageBuilder.randomInfo().build();
        tested.publishMessageReceived(testMessage);

        ArgumentCaptor<Message> publishedMessage = ArgumentCaptor.forClass(Message.class);
        verify(mockRedisTemplate).convertAndSend(eq(MyMessageEventListener.EVENT_RECEIVE_MESSAGE_KEY), publishedMessage.capture());

        MessageAssertions.assertMessageEquals(testMessage, publishedMessage.getValue());
    }

    @Test(expected = NullPointerException.class)
    public void testEmptyPublishThrowsError() {
        tested.publishMessageReceived(null);
    }
}
