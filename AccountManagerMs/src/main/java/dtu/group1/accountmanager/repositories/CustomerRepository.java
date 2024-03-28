package dtu.group1.accountmanager.repositories;

import dtu.group1.common.exceptions.*;
import dtu.group1.common.events.*;
import dtu.group1.common.repositories.EventStore;
import dtu.group1.accountmanager.models.Customer;
import dtu.group1.common.models.AccountID;
import dtu.group1.common.CorrelationID;

import java.util.Collection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.inject.Singleton;
import javax.inject.Inject;

import messaging.MessageQueue;

import javax.inject.Singleton;

import java.util.function.BiConsumer;

public class CustomerRepository {
    private EventStore<AccountID> eventStore;
	public CustomerRepository(MessageQueue q) {
        eventStore = new EventStore(q);
	}

    public void save(Customer customer) {
        eventStore.addEvents(customer.getAccountID(), customer.getAppliedEvents());
        customer.clearAppliedEvents();
    }

    private boolean isDeleted(AccountID id) {
        var lastEvent = eventStore.getEventsFor(id)
            .reduce((first, second) -> second)
            .orElse(null);
        if(lastEvent instanceof CustomerDeletedEvent) {
            return true;
        }
        return false;
    }

    public Customer getCustomer(AccountID id) throws NoSuchCustomerException {
        if(isDeleted(id)) {
            throw new NoSuchCustomerException();
        }
        return Customer.createFromEvents(eventStore.getEventsFor(id));
    }
}
