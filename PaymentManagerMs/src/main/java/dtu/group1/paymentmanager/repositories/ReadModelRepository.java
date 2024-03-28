package dtu.group1.paymentmanager.repositories;

import dtu.group1.common.events.*;
import dtu.group1.paymentmanager.models.*;
import dtu.group1.common.CorrelationID;

import java.util.Collection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Singleton;
import javax.inject.Inject;

import messaging.MessageQueue;


import java.util.function.BiConsumer;

@Singleton
public class ReadModelRepository {
    private ConcurrentMap<CorrelationID, Payment> payments = new ConcurrentHashMap<>();
    private MessageQueue eventQueue;
    private BiConsumer<CorrelationID, Payment> transactionHandle;

    @Inject
    public ReadModelRepository(MessageQueue q) {
        eventQueue = q;
        for (var cls : Payment.getHandlers()) {
            eventQueue.addHandler(cls, this::apply);
        }
    }

    public void addTransactionHandle(BiConsumer<CorrelationID, Payment> th) {
        transactionHandle = th;
    }

    public void apply(Event e) {
        getPayment(e.getCorrelationID()).applyEvent(e);
        if (getPayment(e.getCorrelationID()).readyForTransaction()) {
            transactionHandle.accept(e.getCorrelationID(), getPayment(e.getCorrelationID()));
        }
    }

    private Payment getPayment(CorrelationID corrid) {
        payments.putIfAbsent(corrid, new Payment());
        return payments.get(corrid);
    }

    public Collection<Payment> getPayments() {
        return payments.values();
    }
}
