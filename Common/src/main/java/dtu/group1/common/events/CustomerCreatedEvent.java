package dtu.group1.common.events;

import dtu.group1.common.models.*;
import dtu.group1.common.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class CustomerCreatedEvent extends Event {
    private AccountID id;
    private PersonalInfo info;
    private BankNumber bankNumber;

    public CustomerCreatedEvent(AccountID id, PersonalInfo info, BankNumber bankNumber, CorrelationID correlationID) {
        super(correlationID);
        this.id = id;
        this.info = info;
        this.bankNumber = bankNumber;
    }
}
