package dtu.group1.accountmanager.models;

import dtu.group1.common.models.BankNumber;
import dtu.group1.common.models.PersonalInfo;
import dtu.group1.common.models.AccountID;
import dtu.group1.common.events.*;
import dtu.group1.common.CorrelationID;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.function.BiConsumer;

import lombok.Getter;
import lombok.Setter;
import lombok.AccessLevel;

@Getter // Automatic getter and setters and equals etc
public class Customer {
    private AccountID accountID;
    private PersonalInfo info;
    private BankNumber bankNumber;

    public static Customer create(PersonalInfo info, BankNumber bankNumber) {
        var customer = new Customer();
        customer.applyEvent(new CustomerCreatedEvent(IDFactory.generateUniqueID(), info, bankNumber, null));
        return customer;
    }

    public void update(PersonalInfo info, BankNumber bankNumber) {
        applyEvent(new CustomerUpdatedEvent(accountID, info, bankNumber, null));
    }

    public void delete() {
        applyEvent(new CustomerDeletedEvent(accountID, null));
    }

    private void apply(CustomerCreatedEvent e) {
        accountID = e.getId();
        info = e.getInfo();
        bankNumber = e.getBankNumber();
    }

    private void apply(CustomerUpdatedEvent e) {
        info = e.getInfo();
        bankNumber = e.getBankNumber();
    }

    public static Customer createFromEvents(Stream<Event> events) {
        var customer = new Customer();
        customer.applyAllEvents(events);
        customer.clearAppliedEvents();
        return customer;
    }

    static private Map<Class<? extends Event>, BiConsumer<Customer, Event>> handlers = new HashMap<>();

    static {
		handlers.put(CustomerCreatedEvent.class, (self, e) -> self.apply((CustomerCreatedEvent) e));
		handlers.put(CustomerUpdatedEvent.class, (self, e) -> self.apply((CustomerUpdatedEvent) e));
    }

	@Setter(AccessLevel.NONE)
	private List<Event> appliedEvents = new ArrayList<Event>();

    public void clearAppliedEvents() {
        appliedEvents.clear();
    }

    public void applyEvent(Event event) {
        handlers.getOrDefault(event.getClass(), (self, e) -> self.missingHandle(e)).accept(this, event);
        appliedEvents.add(event);
    }

    public void applyAllEvents(Stream<Event> events) {
        events.forEachOrdered(e -> this.applyEvent(e));
    }

    private void missingHandle(Event e) {
        System.out.println("Tried to apply event without an appropiate method: " + e.getClass());
    }
}
