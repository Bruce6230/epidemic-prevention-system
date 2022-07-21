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
        factory.setHost("192.168.31.152");
        factory.setPort(5672);
        factory.setUsername("GentlemanLin");
        factory.setPassword("lin171820...");
        return factory;
    }
}
