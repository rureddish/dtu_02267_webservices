package dtu.group1.accountmanager.adapter.mq;

import messaging.MessageQueue;
import dtu.group1.accountmanager.*;
import dtu.group1.common.models.*;
import dtu.group1.common.events.*;
import dtu.group1.common.exceptions.*;
import javax.inject.Singleton;
import javax.inject.Inject;
import io.quarkus.runtime.Startup;

@Startup
@Singleton
public class AccountManagerMQService {
    IAccountManager accountManager;
    MessageQueue queue;

    @Inject
    public AccountManagerMQService(IAccountManager am, MessageQueue q) {
        accountManager = am;
        queue = q;
        queue.addHandler(TokensCreationRequestedEvent.class, this::handleTokenCreation);
        queue.addHandler(TokenValidatedForCustomerEvent.class, this::handleTokenValidatedForCustomer);
        queue.addHandler(PaymentRequestedEvent.class, this::handlePaymentRequested);
    }

    public void handleTokenCreation(TokensCreationRequestedEvent e) {
        queue.publish(new CustomerWasValidatedForTokenCreationEvent(e.getAmount(), e.getAccountID(), e.getCorrelationID()));
    }

    public void handleTokenValidatedForCustomer(TokenValidatedForCustomerEvent e) {
        var id = e.getAccountID();
        var corrid = e.getCorrelationID();
        try {
            var banknumber = accountManager.getCustomer(id).getBankNumber();
            queue.publish(new CustomerBankNumberAssignedToPaymentEvent(banknumber, corrid));
        } catch (NoSuchCustomerException ex) {
        }
    }

    public void handlePaymentRequested(PaymentRequestedEvent e) {
        var req = e.getPaymentRequest();
        var corrid = e.getCorrelationID();
        BankNumber banknumber = null;

        try {
            banknumber = accountManager.getMerchant(req.getMid()).getBankNumber();
        queue.publish(new MerchantBankNumberAssignedToPaymentEvent(banknumber, corrid));
        } catch (NoSuchMerchantException ex) {
            ex.printStackTrace();
        }

    }
}
