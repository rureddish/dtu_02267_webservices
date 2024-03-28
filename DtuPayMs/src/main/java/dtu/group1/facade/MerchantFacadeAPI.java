package dtu.group1.facade;

import dtu.group1.common.models.*;
import dtu.group1.common.events.*;
import dtu.group1.common.*;
import dtu.group1.common.exceptions.*;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.concurrent.CompletableFuture;

import messaging.MessageQueue;

@Singleton
public class MerchantFacadeAPI {
    WebTarget paymentService;
    WebTarget merchantService;
    WebTarget reportService;

    private CorrelationMap<CompletableFuture<PaySupplier>> pendingPayments = new CorrelationMap(this.getClass().toString());
    private MessageQueue queue;

    @Inject
    public MerchantFacadeAPI(MessageQueue q) {
        queue = q;
        Client client = ClientBuilder.newClient();
        merchantService = client.target("http://account-manager-ms:8080/");
        paymentService = client.target("http://payment-manager-ms:8080/");
        reportService = paymentService.path("payments");
        q.addHandler(PaymentSucceededEvent.class, this::handlePaymentSucceeded);
        q.addHandler(PaymentFailedEvent.class, this::handlePaymentFailed);
        queue.addHandler(TokenNotValidatedForCustomerEvent.class, this::handleTokenNotValidatedForCustomer);
    }

    private void handleTokenNotValidatedForCustomer(TokenNotValidatedForCustomerEvent e) {
        pendingPayments.get(e.getCorrelationID()).complete(() -> {
            throw new InvalidTokenException(e.getToken());
        });
    }


    public AccountID registerMerchant(Merchant merchant) {
        return merchantService.path("merchants").request().post(Entity.entity(merchant, MediaType.APPLICATION_JSON_TYPE), AccountID.class);
    }

    public boolean pay(PaymentRequest req) throws BankException, InvalidTokenException {
        var fut = new CompletableFuture<PaySupplier>();
        var corrid = pendingPayments.add(fut);
        queue.publish(new PaymentRequestedEvent(req, corrid));
        return fut.join().get();
    }

    public Response getMerchant(AccountID mid) {
        return merchantService
                .path("merchants")
                .path(mid.getIdentifier())
                .request()
                .get();
    }

    public Response updateMerchant(AccountID mid, Merchant merchant) {
        return merchantService
                .path("merchants")
                .path(mid.getIdentifier())
                .request()
                .put(Entity.entity(merchant, MediaType.APPLICATION_JSON_TYPE));
    }

    public Response deleteMerchant(AccountID mid) {
        return merchantService
                .path("merchants")
                .path(mid.getIdentifier()).request().delete();
    }


    public Response merchantReport(AccountID mid) {
        var target = reportService
                .path("merchant")
                .path(mid.getIdentifier());

        return target.request().get();
    }

    public void handlePaymentSucceeded(PaymentSucceededEvent e) {
        pendingPayments.get(e.getCorrelationID()).complete(() -> true);
    }

    public void handlePaymentFailed(PaymentFailedEvent e) {
        pendingPayments.get(e.getCorrelationID()).complete(() -> {
            throw e.getBankException();
        });
    }
}

@FunctionalInterface
interface PaySupplier {
    boolean get() throws BankException, InvalidTokenException;
}
