package com.iqmsoft.redis.websocket.web.rest;

import java.net.URI;
import java.util.Collection;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.iqmsoft.redis.websocket.domain.model.Message;
import com.iqmsoft.redis.websocket.domain.model.Message.MessageBuilder;

import com.iqmsoft.redis.websocket.domain.service.MessageService;
import com.iqmsoft.redis.websocket.web.events.MessageEventPublisher;
import com.iqmsoft.redis.websocket.web.events.MyMessageBroadcaster;
import com.iqmsoft.redis.websocket.web.model.MyMessage;

@Controller
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private MessageEventPublisher messageEventPublisher;

    
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);
    
    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public ResponseEntity<Void> broadcast(@RequestBody MyMessage message, HttpServletRequest request) {

        Message receivedMessage = createBuilderFromBasicMessage(message)
                .sentBy(request.getRemoteAddr())
                .build();

        // Publish message received event
        messageEventPublisher.publishMessageReceived(receivedMessage);
        
   

        logger.debug("Broad S:[{}]", receivedMessage);
   	    logger.info("Broad S:[{}]", receivedMessage);
   	 
   	 
        
        return ResponseEntity
                .created(URI.create(getServerUrl(request) + "/api/message/" + receivedMessage.getId()))
                .build();
    }

    /**
     * Rest resource for querying all the persisted messages
     */
    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public ResponseEntity<Collection<Message>> getMessages() {

        Collection<Message> findAll = messageService.findAll();

        return ResponseEntity.ok(findAll);
    }

    /**
     * Rest resource for querying all the persisted messages
     */
    @RequestMapping(value = "/message/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@PathVariable String id) {

        messageService.delete(id);

        logger.debug("Broad S:[{}] Delete");
   	    
        return ResponseEntity.ok().build();
    }

    /**
     * WebSocket channel for receiving {@link MyMessage} object from websocket clients
     */
    @MessageMapping("/broadcast")
    @SendTo(MyMessageBroadcaster.WEBSOCKET_MESSAGE_TOPIC_PATH)
    public Message socketBroadcast(MyMessage message, SimpMessageHeaderAccessor headerAccessor
    		) {
       
    	 String sessionId = headerAccessor.getSessionId(); // Session ID
    	 MessageHeaders m = headerAccessor.getMessageHeaders();
    	 System.out.println("Client Addresss");
    	 System.out.println(m.toString());
    	
    	 logger.debug("Broad S:[{}]", sessionId);
    	 logger.info("Broad S:[{}]", sessionId);
    	 
    	 logger.debug("Broad :[{}]", m.toString());
    	 logger.info("Broad :[{}]", m.toString());
    	 
        return createBuilderFromBasicMessage(message).build();
    }

    private MessageBuilder createBuilderFromBasicMessage(MyMessage message) {
        return MessageBuilder.empty()
        	    .withContent(message.getContent())
                .withType(message.getType());
    }

    private String getServerUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setMessageEventPublisher(MessageEventPublisher messageEventPublisher) {
        this.messageEventPublisher = messageEventPublisher;
    }

}
