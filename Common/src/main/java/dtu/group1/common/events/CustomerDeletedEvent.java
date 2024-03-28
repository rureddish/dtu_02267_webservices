package dtu.group1.common.events;

import dtu.group1.common.models.*;
import dtu.group1.common.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CustomerDeletedEvent extends Event {
    private AccountID id;

    public CustomerDeletedEvent(AccountID id, CorrelationID correlationID) {
        super(correlationID);
        this.id = id;
    }
}
