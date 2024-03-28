package dtu.group1.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.UUID;

public class CorrelationMap<T> {
    ConcurrentMap<CorrelationID, T> map = new ConcurrentHashMap<>();
    String domain;

    public CorrelationMap(String domain) {
        this.domain = domain;
    }

    public T get(CorrelationID id) {
        return map.get(id);
    }

    public CorrelationID add(T value) {
        var id = genID();
        map.put(id, value);
        return id;
    }

    private CorrelationID genID() {
        CorrelationID id;
        do {
            id = new CorrelationID(domain + UUID.randomUUID().toString());
        } while (map.containsKey(id));
        return id;
    }
}
