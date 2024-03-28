package dtu.group1.common.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

import lombok.NonNull;
import messaging.MessageQueue;
import dtu.group1.common.events.Event;

public class EventStore<K> {
    private ConcurrentMap<K, List<Event>> store = new ConcurrentHashMap<>();

	private MessageQueue eventBus;

	public EventStore(MessageQueue bus) {
		this.eventBus = bus;
	}

	public void addEvent(K id, Event event) {
        store.putIfAbsent(id, new ArrayList<Event>());
		store.get(id).add(event);
		eventBus.publish(event);
	}
	
	public Stream<Event> getEventsFor(K id) {
        store.putIfAbsent(id, new ArrayList<Event>());
		return store.get(id).stream();
	}

	public void addEvents(@NonNull K id, List<Event> appliedEvents) {
		appliedEvents.stream().forEach(e -> addEvent(id, e));
	}
}
