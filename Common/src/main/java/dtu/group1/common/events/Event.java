package dtu.group1.common.events;


import dtu.group1.common.*;
import lombok.Getter;
import messaging.Message;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public abstract class Event implements Message {
    private static final long serialVersionUID = -8571080289905090781L;
    private CorrelationID correlationID;
}
