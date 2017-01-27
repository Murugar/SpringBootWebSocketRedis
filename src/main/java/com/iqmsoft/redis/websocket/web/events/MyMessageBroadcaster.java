package com.iqmsoft.redis.websocket.web.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.iqmsoft.redis.websocket.domain.model.Message;


@Component
public class MyMessageBroadcaster {

    public static final String WEBSOCKET_MESSAGE_TOPIC_PATH = "/topic/messages";

    public static final String EVENT_RECEIVE_MESSAGE_KEY = "MyMessages";

    private static final Logger logger = LoggerFactory.getLogger(MyMessageBroadcaster.class);

    @Autowired
    private SimpMessagingTemplate brokerMessagingTemplate;

    public void handleMessage(Message message, String channel) {

        logger.debug("Message receive event fired on channel:[{}]", channel);

        // Push on websocket
        brokerMessagingTemplate.convertAndSend(WEBSOCKET_MESSAGE_TOPIC_PATH, message);
    }

    public void setBrokerMessagingTemplate(SimpMessagingTemplate brokerMessagingTemplate) {
        this.brokerMessagingTemplate = brokerMessagingTemplate;
    }

}
