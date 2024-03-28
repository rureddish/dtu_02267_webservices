package dtu.group1.accountmanager;

import java.util.ArrayList;

import dtu.group1.common.exceptions.NoSuchMerchantException;
import static org.mockito.Mockito.mock;
import dtu.group1.common.models.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.Assert.*;

import messaging.MessageQueue;

public class StepDefinitions {
    AccountID cid;
    AccountID mid;
    BankNumber bankNumber;
    ArrayList<AccountID> ids = new ArrayList<>();
    IAccountManager am = new AccountManager(mock(MessageQueue.class));

    @Given("a customer registers")
    public void aCustomerRegisters() {
        cid = am.registerCustomer(new Customer(new PersonalInfo("firstname", "lastname", "cpr"), new BankNumber("banknumber")));
        ids.add(cid);
    }

    @Given("a merchant registers")
    public void aMerchantRegisters() {
        mid = am.registerMerchant(new Merchant(new PersonalInfo("firstname", "lastname", "cpr"), new BankNumber("banknumber")));
        ids.add(mid);
    }

    @Then("the IDs are unique")
    public void theIDsAreUnique() {
        ArrayList<AccountID> seenIds = new ArrayList<>();
        for (var id : ids) {
            assertFalse(seenIds.contains(id));
            seenIds.add(id);
        }
    }

    @Given("a customer registers with bank number {string}")
    public void aCustomerRegistersWithBankNumber(String string) {
        cid = am.registerCustomer(new Customer(new PersonalInfo("firstname", "lastname", "cpr"), new BankNumber(string)));
        ids.add(cid);
    }

    @When("the customer requests the bank number")
    public void theCustomerRequestsTheBankNumber() {
        try {
            bankNumber = am.getCustomer(cid).getBankNumber();
        } catch (Exception e) {
        }
    }

    @Given("a merchant registers with bank number {string}")
    public void aMerchantRegistersWithBankNumber(String string) {
        mid = am.registerMerchant(new Merchant(new PersonalInfo("firstname", "lastname", "cpr"), new BankNumber(string)));
        ids.add(mid);
    }

    @When("the merchant requests the bank number")
    public void theMerchantRequestsTheBankNumber() {
        try {
            bankNumber = am.getMerchant(mid).getBankNumber();
        } catch (NoSuchMerchantException e) {
            e.printStackTrace();
        }

    }

    @Then("the bank number is {string}")
    public void theBankNumberIs(String string) {
        bankNumber.equals(string);
    }
}
