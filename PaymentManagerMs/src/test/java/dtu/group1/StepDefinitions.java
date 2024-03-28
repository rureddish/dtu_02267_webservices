package dtu.group1.paymentmanager;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.timeout;

import dtu.group1.common.exceptions.BankException;
import dtu.group1.common.models.*;
import dtu.group1.common.events.*;
import dtu.group1.paymentmanager.*;
import dtu.group1.paymentmanager.repositories.*;

import messaging.MessageQueue;
import messaging.Message;

import io.cucumber.java.PendingException;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Test;

import static org.junit.Assert.*;

import dtu.group1.common.*;
import dtu.group1.common.models.*;
import dtu.ws.fastmoney.BankService;

public class StepDefinitions {
    Token token1 = new Token("token1");
    Token token2 = new Token("token1");

    CorrelationID corrid1;
    CorrelationID corrid2;

    BankService bank = mock(BankService.class);
    ReadModelRepository repo = new ReadModelRepository(mock(MessageQueue.class));
    IPaymentManager pm = new PaymentManager(mock(MessageQueue.class), repo, bank);

    @When("a payment is requested")
    public void aPaymentIsRequested() {
        corrid1 = new CorrelationID("1");
        repo.apply(new PaymentRequestedEvent(new PaymentRequest(token1, new AccountID("merchant1id"), new BigDecimal(1)), corrid1));
    }

    @When("a second payment is requested")
    public void aSecondPaymentIsRequested() {
        corrid2 = new CorrelationID("2");
        repo.apply(new PaymentRequestedEvent(new PaymentRequest(token1, new AccountID("merchant2id"), new BigDecimal(2)), corrid2));
    }

    @When("the customer and merchant bank numbers are assigned to the second payment")
    public void theCustomerAndMerchantBankNumbersAreAssignedToTheSecondPayment() {
        repo.apply(new CustomerBankNumberAssignedToPaymentEvent(new BankNumber("customer2"), corrid2));
        repo.apply(new MerchantBankNumberAssignedToPaymentEvent(new BankNumber("merchant2"), corrid2));
    }

    @Then("a bank transaction is initiated for the second payment")
    public void aBankTransactionIsInitiatedForTheSecondPayment() {
        try {
            verify(bank, timeout(100)).transferMoneyFromTo(any(), any(), eq(new BigDecimal(2)), any());
        } catch (Exception e) {
            e.getMessage();
            fail();
        }
    }

    @When("the customer and merchant bank numbers are assigned to the first payment")
    public void theCustomerAndMerchantBankNumbersAreAssignedToTheFirstPayment() {
        repo.apply(new CustomerBankNumberAssignedToPaymentEvent(new BankNumber("customer1"), corrid1));
        repo.apply(new MerchantBankNumberAssignedToPaymentEvent(new BankNumber("merchant1"), corrid1));
    }

    @Then("a bank transaction is initiated for the first payment")
    public void aBankTransactionIsInitiatedForTheFirstPayment() {
        try {
            verify(bank, timeout(100)).transferMoneyFromTo(any(), any(), eq(new BigDecimal(2)), any());
        } catch (Exception e) {
            e.getMessage();
            fail();
        }
    }
}
