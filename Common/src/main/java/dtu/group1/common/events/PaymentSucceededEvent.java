package dtu.group1.common.events;

import dtu.group1.common.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class PaymentSucceededEvent extends Event {
    public PaymentSucceededEvent(CorrelationID correlationID) {
        super(correlationID);
    }
}
