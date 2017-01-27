package com.iqmsoft.redis.websocket.web.events;

import com.iqmsoft.redis.websocket.domain.model.Message;


public interface MessageEventPublisher {

    void publishMessageReceived(Message message);

}
