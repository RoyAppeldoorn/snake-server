package com.mpsnake.backend;

import com.mpsnake.backend.dispatcher.Dispatcher;
import com.mpsnake.backend.dispatcher.MessageDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {
    @Bean
    @Autowired
    public Dispatcher dispatcher(MessageDispatcher messageDispatcher) {
        return messageDispatcher;
    }
}
