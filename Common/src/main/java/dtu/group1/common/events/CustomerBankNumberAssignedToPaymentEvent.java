package dtu.group1.common.events;

import dtu.group1.common.models.*;
import dtu.group1.common.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CustomerBankNumberAssignedToPaymentEvent extends Event {
    private BankNumber bankNumber;

    public CustomerBankNumberAssignedToPaymentEvent(BankNumber bankNumber, CorrelationID correlationID) {
        super(correlationID);
        this.bankNumber = bankNumber;
    }
}
