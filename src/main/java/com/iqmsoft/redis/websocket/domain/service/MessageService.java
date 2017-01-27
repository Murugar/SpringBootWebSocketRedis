package com.iqmsoft.redis.websocket.domain.service;

import java.util.Collection;

import com.iqmsoft.redis.websocket.domain.model.Message;


public interface MessageService {

    Message create(Message message);
    Collection<Message> findAll();
    Message findById(String id);
    void delete(String id);

}
