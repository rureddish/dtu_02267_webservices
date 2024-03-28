package dtu.group1.common.events;

import dtu.group1.common.exceptions.*;
import dtu.group1.common.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class PaymentFailedEvent extends Event {
    private BankException bankException;

    public PaymentFailedEvent(BankException bankException, CorrelationID correlationID) {
        super(correlationID);
        this.bankException = bankException;
    }
}
