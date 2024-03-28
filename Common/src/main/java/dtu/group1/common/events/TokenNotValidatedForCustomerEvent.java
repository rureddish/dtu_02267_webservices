package dtu.group1.common.events;

import dtu.group1.common.models.*;
import dtu.group1.common.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class TokenNotValidatedForCustomerEvent extends Event {
    private Token token;

    public TokenNotValidatedForCustomerEvent(Token token, CorrelationID correlationID) {
        super(correlationID);
        this.token = token;
    }
}
