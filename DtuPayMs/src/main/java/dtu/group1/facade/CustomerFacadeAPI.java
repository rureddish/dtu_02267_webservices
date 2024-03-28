package dtu.group1.facade;

import dtu.group1.common.exceptions.InvalidTokenCreationRequestException;
import dtu.group1.common.models.*;
import dtu.group1.common.*;
import dtu.group1.common.events.*;
import java.util.concurrent.CompletableFuture;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import messaging.MessageQueue;

@Singleton
public class CustomerFacadeAPI {
    private WebTarget customerService;
    private WebTarget reportService;
    private WebTarget tokenService;

    private CorrelationMap<CompletableFuture<TokenSupplier>> pendingTokenCreation = new CorrelationMap(this.getClass().toString());
    private MessageQueue queue;

    @Inject
    public CustomerFacadeAPI(MessageQueue q) {
        queue = q;
        Client client = ClientBuilder.newClient();
        customerService = client.target("http://account-manager-ms:8080/");
        tokenService = client.target("http://token-manager-ms:8080/");
        reportService = client.target("http://payment-manager-ms:8080/payments");
        queue.addHandler(TokensCreatedEvent.class, this::handleTokensCreated);
        queue.addHandler(TokensCreationFailedEvent.class, this::handleTokensNotCreated);
    }

    private void handleTokensNotCreated(TokensCreationFailedEvent e) {
        pendingTokenCreation.get(e.getCorrelationID()).complete(() -> {
            throw new InvalidTokenCreationRequestException();
        });
    }

    public Response registerCustomer(Customer customer) {
        return customerService
                .path("customers")
                .request()
                .post(Entity.entity(customer, MediaType.APPLICATION_JSON_TYPE));
    }

    public List<Token> createTokens(int amount, AccountID cid) throws InvalidTokenCreationRequestException {
        var fut = new CompletableFuture<TokenSupplier>();
        var corrid = pendingTokenCreation.add(fut);
        queue.publish(new TokensCreationRequestedEvent(amount, cid, corrid));
        return fut.join().get();
    }

    public Response getCustomer(AccountID cid) {
        return customerService
                .path("customers")
                .path(cid.getIdentifier())
                .request()
                .get();
    }

    public Response getAllCustomers() {
        return customerService
                .path("customers")
                .request()
                .get();
    }

    public Response updateCustomer(AccountID cid, Customer customer) {
        return customerService
                .path("customers")
                .path(cid.getIdentifier())
                .request()
                .put(Entity.entity(customer, MediaType.APPLICATION_JSON_TYPE));
    }

    public Response deleteCustomer(AccountID cid) {
        System.out.println("delete customer facadeAPI");
        return customerService
                .path("customers")
                .path(cid.getIdentifier())
                .request()
                .delete();
    }

    public Response customerReport(AccountID cid) {
        var target = reportService
                .path("customer")
                .path(cid.getIdentifier());

        return target.request().get();
    }

    public void handleTokensCreated(TokensCreatedEvent e) {
        pendingTokenCreation.get(e.getCorrelationID()).complete(() -> e.getTokens());
    }
}

@FunctionalInterface
interface TokenSupplier {
    List<Token> get() throws InvalidTokenCreationRequestException;
}
