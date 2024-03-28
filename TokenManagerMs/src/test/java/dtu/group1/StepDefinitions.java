package dtu.group1.tokenmanager;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.timeout;

import dtu.group1.common.exceptions.InvalidTokenCreationRequestException;
import dtu.group1.common.models.*;
import dtu.group1.common.*;
import dtu.group1.common.events.*;
import dtu.group1.tokenmanager.adapters.mq.*;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;

import messaging.MessageQueue;

import java.util.List;
import java.util.HashSet;
import java.math.BigDecimal;

public class StepDefinitions {
    TokenManager tm = new TokenManager();
    MessageQueue q = mock(MessageQueue.class);
    TokenManagerMQService tms = new TokenManagerMQService(tm, q);


    AccountID customer;
    Token token;
    List<Token> tokens;
    AccountID cid;

    @Given("there is a customer")
    public void thereIsACustomer() {
        customer = new AccountID("customer");
    }

    @When("the customer is creating {int} tokens")
    public void theCustomerIsCreatingTokens(Integer int1) throws InvalidTokenCreationRequestException {
        tokens = tm.createTokens(int1, cid);
    }

    @Then("the tokens are unique")
    public void theTokensAreUnique() {
        HashSet<Token> set = new HashSet<>();
        set.addAll(tokens);
        assertEquals(tokens.size(), set.size());
    }

    @Given("a customer has a token")
    public void aCustomerHasAToken() throws InvalidTokenCreationRequestException {
        cid = new AccountID("customer");
        token = tm.createTokens(1, cid).get(0);
    }

    @When("a payment request event with the same token is published")
    public void aPaymentRequestEventWithTheSameTokenIsPublished() {
        tms.handlePaymentRequested(new PaymentRequestedEvent(new PaymentRequest(token, new AccountID(), new BigDecimal(41)), new CorrelationID()));
    }

    @Then("a token validated for customer event is published for the customer")
    public void aTokenValidatedForCustomerEventIsPublishedForTheCustomer() {
        verify(q, timeout(100)).publish(new TokenValidatedForCustomerEvent(cid, new CorrelationID()));
    }
}
