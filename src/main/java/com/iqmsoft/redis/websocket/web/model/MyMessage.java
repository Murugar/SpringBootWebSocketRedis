package com.iqmsoft.redis.websocket.web.model;

import com.iqmsoft.redis.websocket.domain.model.MessageType;


public class MyMessage {

    private String content;

    private MessageType type;

    public MyMessage() {
    }

    public MyMessage(String content, MessageType type) {
        this.content = content;
        this.type = type;
    }

    protected void setContent(String content) {
        this.content = content;
    }

    protected void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public MessageType getType() {
        return type;
    }

}
