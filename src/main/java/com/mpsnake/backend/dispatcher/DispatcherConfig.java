package com.mpsnake.backend.dispatcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DispatcherConfig {
    @Bean
    @Autowired
    public MessageDispatcher messageDispatcher() {
        return new MessageDispatcher();
    }
}
