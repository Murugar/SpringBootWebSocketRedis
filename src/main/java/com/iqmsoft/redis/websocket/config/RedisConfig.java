package com.iqmsoft.redis.websocket.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.iqmsoft.redis.websocket.domain.model.Message;
import com.iqmsoft.redis.websocket.domain.redis.MyMessageEventListener;
import com.iqmsoft.redis.websocket.web.events.MyMessageBroadcaster;

@Configuration
@Profile("!embedded")
public class RedisConfig {

    @Bean
    public Jackson2JsonRedisSerializer<Message> messageRedisSerializer() {

      
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
      
        Jackson2JsonRedisSerializer<Message> serializer = new Jackson2JsonRedisSerializer<>(Message.class);
        serializer.setObjectMapper(objectMapper);
        return serializer;
    }

    @Bean
    public RedisTemplate<String, Message> messageRedisTemplate(RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, Message> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setValueSerializer(messageRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(MyMessageEventListener eventListener) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(eventListener);
        adapter.setSerializer(messageRedisSerializer());
        return adapter;
    }

    @Bean
    public MessageListenerAdapter messageBroadcasterAdapter(MyMessageBroadcaster eventListener) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(eventListener);
        adapter.setSerializer(messageRedisSerializer());
        return adapter;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory, MyMessageEventListener eventListener,
            MyMessageBroadcaster broadcastListener) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(messageListenerAdapter(eventListener),
                Arrays.asList(new ChannelTopic(MyMessageEventListener.EVENT_RECEIVE_MESSAGE_KEY)));
        container.addMessageListener(messageBroadcasterAdapter(broadcastListener),
                Arrays.asList(new ChannelTopic(MyMessageBroadcaster.EVENT_RECEIVE_MESSAGE_KEY)));

        return container;
    }
}
