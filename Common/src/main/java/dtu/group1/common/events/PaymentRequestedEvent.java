package dtu.group1.common.events;

import dtu.group1.common.models.*;
import dtu.group1.common.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class PaymentRequestedEvent extends Event {
    private PaymentRequest paymentRequest;

    public PaymentRequestedEvent(PaymentRequest paymentRequest, CorrelationID correlationID) {
        super(correlationID);
        this.paymentRequest = paymentRequest;
    }
}
