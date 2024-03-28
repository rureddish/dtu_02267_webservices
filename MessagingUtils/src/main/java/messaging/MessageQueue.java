package messaging;

import java.util.function.Consumer;

public interface MessageQueue {

    void publish(Message message);

    <T extends Message> void addHandler(Class<T> event, Consumer<T> handler);

}
