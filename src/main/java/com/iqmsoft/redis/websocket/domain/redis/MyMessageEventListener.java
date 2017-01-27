package com.iqmsoft.redis.websocket.domain.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iqmsoft.redis.websocket.domain.model.Message;
import com.iqmsoft.redis.websocket.domain.service.MessageService;

@Component
public class MyMessageEventListener {

    public static final String EVENT_RECEIVE_MESSAGE_KEY = "MyMessages";

    private static final Logger logger = LoggerFactory.getLogger(MyMessageEventListener.class);

    @Autowired
    private MessageService messageService;

    public void handleMessage(Message message, String channel) {

        logger.debug("Message event fired on channel:[{}]", channel);

        messageService.create(message);
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

}
