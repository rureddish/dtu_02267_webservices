package dtu.group1.common.events;

import dtu.group1.common.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class TokensCreationFailedEvent extends Event {

    public TokensCreationFailedEvent(CorrelationID correlationID) {
        super(correlationID);
    }
}

