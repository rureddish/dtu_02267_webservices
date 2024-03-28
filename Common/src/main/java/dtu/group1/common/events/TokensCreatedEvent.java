package dtu.group1.common.events;

import dtu.group1.common.models.*;
import dtu.group1.common.*;
import java.util.List;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class TokensCreatedEvent extends Event {
    private List<Token> tokens;

    public TokensCreatedEvent(List<Token> tokens, CorrelationID correlationID) {
        super(correlationID);
        this.tokens = tokens;
    }
}
