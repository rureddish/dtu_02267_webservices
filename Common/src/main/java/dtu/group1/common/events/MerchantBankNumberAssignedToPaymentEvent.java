package dtu.group1.common.events;

import dtu.group1.common.models.*;
import dtu.group1.common.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class MerchantBankNumberAssignedToPaymentEvent extends Event {
    private BankNumber bankNumber;

    public MerchantBankNumberAssignedToPaymentEvent(BankNumber bankNumber, CorrelationID correlationID) {
        super(correlationID);
        this.bankNumber = bankNumber;
    }
}
