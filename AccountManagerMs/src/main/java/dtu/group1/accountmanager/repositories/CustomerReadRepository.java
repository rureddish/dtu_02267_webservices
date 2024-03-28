package dtu.group1.accountmanager.repositories;

import dtu.group1.common.exceptions.*;
import dtu.group1.common.events.*;
import dtu.group1.common.models.Customer;
import dtu.group1.common.models.AccountID;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.List;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import messaging.MessageQueue;

import java.util.function.BiConsumer;

public class CustomerReadRepository {
    private ConcurrentMap<AccountID, Customer> customers = new ConcurrentHashMap<>();
    private MessageQueue eventQueue;

	public CustomerReadRepository(MessageQueue q) {
        eventQueue = q;
		q.addHandler(CustomerCreatedEvent.class, (e) -> this.apply((CustomerCreatedEvent) e));
		q.addHandler(CustomerUpdatedEvent.class, (e) -> this.apply((CustomerUpdatedEvent) e));
		q.addHandler(CustomerDeletedEvent.class, (e) -> this.apply((CustomerDeletedEvent) e));
	}

    private void apply(CustomerCreatedEvent e) {
        customers.put(e.getId(), new Customer(e.getInfo(), e.getBankNumber()));
    }

    private void apply(CustomerUpdatedEvent e) {
        customers.put(e.getId(), new Customer(e.getInfo(), e.getBankNumber()));
    }

    private void apply(CustomerDeletedEvent e) {
        System.out.println("Deleted customer");
        customers.remove(e.getId());
    }

    public Customer getCustomer(AccountID id) throws NoSuchCustomerException {
        if (!customers.containsKey(id)) {
            throw new NoSuchCustomerException();
        }
        return customers.get(id);
    }

    public List<Customer> getCustomers() {
        return customers.values()
            .stream()
            .collect(Collectors.toList());
    }
}
