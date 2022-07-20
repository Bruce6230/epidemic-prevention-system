package com.makiyo.task;

import com.makiyo.entity.MessageEntity;
import com.makiyo.entity.MessageRefEntity;
import com.makiyo.exception.EpsException;
import com.makiyo.service.MessageService;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Makiyo
 * @date 2022/7/21 3:02
 */
@Component
@Slf4j
public class MessageTask {
    @Autowired
    private ConnectionFactory factory;

    @Autowired
    private MessageService messageService;

    public void send(String topic, MessageEntity entity) {
        String id = messageService.insertMessage(entity);
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();
        ) {
            channel.queueDeclare(topic, true, false, false, null);
            HashMap map = new HashMap();
            map.put("messageId", id);
            AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().headers(map).build();
            channel.basicPublish("", topic, properties, entity.getMsg().getBytes());
            log.debug("消息发送成功");
        } catch (Exception e) {
            log.error("执行异常", e);
            throw new EpsException("向MQ发送消息失败");
        }
    }

    @Async
    public void sendAsync(String topic, MessageEntity entity) {
        send(topic, entity);
    }

    public int receive(String topic) {
        int i = 0;
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();
        ) {
            channel.queueDeclare(topic, true, false, false, null);
            while (true) {
                GetResponse response = channel.basicGet(topic, false);
                if (response != null) {
                    AMQP.BasicProperties properties = response.getProps();
                    Map<String, Object> map = properties.getHeaders();
                    String messageId = map.get("messageId").toString();
                    byte[] body = response.getBody();
                    String message = new String(body);
                    log.debug("从RabbitMQ接收的消息：" + message);

                    MessageRefEntity entity = new MessageRefEntity();
                    entity.setMessageId(messageId);
                    entity.setReceiverId(Integer.parseInt(topic));
                    entity.setReadFlag(false);
                    entity.setLastFlag(true);
                    messageService.insertRef(entity);
                    long deliveryTag = response.getEnvelope().getDeliveryTag();
                    channel.basicAck(deliveryTag, false);
                    i++;
                }
                else {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("执行异常", e);
            throw new EpsException("接收消息失败");
        }
        return i;
    }

    @Async
    public int receiveAsync(String topic) {
        return receive(topic);
    }

    public void deleteQueue(String topic){
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel();
        ) {
            channel.queueDelete(topic);
            log.debug("消息队列成功删除");
        }catch (Exception e) {
            log.error("删除队列失败", e);
            throw new EpsException("删除队列失败");
        }
    }

    @Async
    public void deleteQueueAsync(String topic){
        deleteQueue(topic);
    }
}
