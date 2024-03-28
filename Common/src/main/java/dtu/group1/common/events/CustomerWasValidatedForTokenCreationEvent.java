package dtu.group1.common.events;

import dtu.group1.common.models.*;
import dtu.group1.common.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CustomerWasValidatedForTokenCreationEvent extends Event {
    private int amount;
    private AccountID accountID;

    public CustomerWasValidatedForTokenCreationEvent(int amount, AccountID accountID, CorrelationID correlationID) {
        super(correlationID);
        this.amount = amount;
        this.accountID = accountID;
    }
}
