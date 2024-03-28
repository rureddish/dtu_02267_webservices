package dtu.group1.accountmanager;

import javax.enterprise.inject.Produces;
import javax.inject.Singleton;

import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;

public class Producer {
    @Produces
    public MessageQueue getMessageQueue() {
        return new RabbitMqQueue("rabbitMq");
    }
}
