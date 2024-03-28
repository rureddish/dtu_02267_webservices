package dtu.group1.accountmanager;

import javax.enterprise.inject.Produces;

import messaging.MessageQueue;
import messaging.implementations.RabbitMqQueue;

import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.BankService;

public class Producer {
    @Produces
    public MessageQueue getMessageQueue() {
        return new RabbitMqQueue("rabbitMq");
    }

    @Produces
    public BankService getBankService() {
        return new BankServiceService().getBankServicePort();
    }
}
