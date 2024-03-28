package dtu.group1.tokenmanager.adapters.mq;

import messaging.MessageQueue;

import dtu.group1.tokenmanager.*;
import dtu.group1.common.models.*;
import dtu.group1.common.events.*;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.quarkus.runtime.Startup;

@Startup
@Singleton
public class TokenManagerMQService {
    private final ITokenManager tokenManager;
    private final MessageQueue queue;

    @Inject
    public TokenManagerMQService(ITokenManager tm, MessageQueue q) {
        tokenManager = tm;
        queue = q;
        q.addHandler(PaymentRequestedEvent.class, this::handlePaymentRequested);
        q.addHandler(CustomerWasValidatedForTokenCreationEvent.class, this::handleCustomerWasValidatedForTokenCreation);
    }

    public void handlePaymentRequested(PaymentRequestedEvent e) {
        var req = e.getPaymentRequest();
        var corrid = e.getCorrelationID();
        Token token = e.getPaymentRequest().getToken();
        if (tokenManager.isTokenValid(token)) {
            var cid = tokenManager.useToken(req.getToken());
            queue.publish(new TokenValidatedForCustomerEvent(cid, corrid));
        } else {
            queue.publish(new TokenNotValidatedForCustomerEvent(token, corrid));
        }
    }

    public void handleCustomerWasValidatedForTokenCreation(CustomerWasValidatedForTokenCreationEvent e) {
        var tokens = tokenManager.createTokens(e.getAmount(), e.getAccountID());
        if (!tokens.isEmpty()) {
            queue.publish(new TokensCreatedEvent(tokens, e.getCorrelationID()));
        } else {
            queue.publish(new TokensCreationFailedEvent(e.getCorrelationID()));
        }
    }
}
