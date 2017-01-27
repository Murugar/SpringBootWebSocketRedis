package com.iqmsoft.redis.websocket.web.events;

import static com.google.common.base.Preconditions.checkNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.iqmsoft.redis.websocket.domain.model.Message;
import com.iqmsoft.redis.websocket.domain.redis.MyMessageEventListener;


@Profile("!embedded")
@Component
public class MessageEventPublisherImpl implements MessageEventPublisher {

    @Autowired
    private RedisTemplate<String, Message> messageRedisTemplate;

    @Override
    public void publishMessageReceived(Message message) {
        
        messageRedisTemplate.convertAndSend(MyMessageEventListener.EVENT_RECEIVE_MESSAGE_KEY, checkNotNull(message, "The received message must not be null!"));
    }

    public void setMessageRedisTemplate(RedisTemplate<String, Message> messageRedisTemplate) {
        this.messageRedisTemplate = messageRedisTemplate;
    }

}
