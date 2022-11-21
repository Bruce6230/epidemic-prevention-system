package com.makiyo.config;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Makiyo
 * @date 2022/7/21 2:47
 */
@Configuration
public class RabbitMQConfig {
    @Bean
    public ConnectionFactory getFactory(){
        ConnectionFactory factory=new ConnectionFactory();
        factory.setHost("192.168.137.109");
        factory.setPort(5672);
        return factory;
    }
}
