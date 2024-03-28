package dtu.group1.common.events;

import dtu.group1.common.models.*;
import dtu.group1.common.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class TokenValidatedForCustomerEvent extends Event {
    private AccountID accountID;

    public TokenValidatedForCustomerEvent(AccountID accountID, CorrelationID correlationID) {
        super(correlationID);
        this.accountID = accountID;
    }
}
