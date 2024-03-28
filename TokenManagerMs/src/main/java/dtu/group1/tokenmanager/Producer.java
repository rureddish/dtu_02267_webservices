package dtu.group1.tokenmanager;

import javax.enterprise.inject.Produces;

import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;

public class Producer {
    @Produces
    public MessageQueue getMessageQueue() {
        return new RabbitMqQueue("rabbitMq");
    }
}
