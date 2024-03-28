package dtu.group1;

import apis.ClientException;
import apis.CustomerAPI;
import apis.MerchantAPI;
import apis.ManagerAPI;
import dtu.group1.common.models.*;
import dtu.ws.fastmoney.*;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import models.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.*;

public class StepDefinitions {
    Customer customer;
    Merchant merchant;
    AccountID cid;
    AccountID cid2;
    AccountID mid;
    BankService bankService = new BankServiceService().getBankServicePort();
    CustomerAPI customerAPI = new CustomerAPI();
    MerchantAPI merchantAPI = new MerchantAPI();
    ManagerAPI managerAPI = new ManagerAPI();
    private List<String> accountIds = new ArrayList<>();
    private Queue<Token> tokens = new LinkedList<>();
    private Queue<Token> invalidTokens = new LinkedList<>();
    private Boolean invalidAmount = false;
    private Boolean invalidUser = false;
    private BigDecimal paymentAmount;
    private BigDecimal balance;
    private boolean unknownCustomer = false;
    private boolean invalidToken = false;
    private Token invalidCustomerToken;
    private Token usedToken;
    private boolean invalidAccountId = false;
    private List<Payment> managerReport;
    private List<CustomerPaymentView> customerReport;
    private List<MerchantPaymentView> merchantReport;
    private boolean invalidCustomerBankNumber;
    private BankNumber invalidBankNr;
    private boolean error;
    private int errorStatus;
    private String errorMessage;

    @Given("a customer with first name {string} last name {string} CPR {string} and bank a account with balance {int}")
    public void aCustomerWithFirstNameLastNameCPRAndBankAAccountWithBalance
            (String string, String string2, String string3, int int1) {
        User user = new User();
        user.setFirstName(string);
        user.setLastName(string2);
        user.setCprNumber(string3);
        customer = new Customer(new PersonalInfo(string, string2, string3), createAccountWithBalance(user, BigDecimal.valueOf(int1)));
    }

    @When("the customer is registered to DTUPay")
    public void theCustomerIsRegisteredToDTUPay() {
        cid = customerAPI.registerCustomer(customer);
    }

    @Then("customer's account ID is returned")
    public void customerSAccountIDIsReturned() {
        assertNotNull(cid);
    }

    @Then("merchant's account ID is returned")
    public void merchantSAccountIDIsReturned() {
        assertNotNull(mid);
    }

    @Given("a merchant with first name {string} last name {string} CPR {string} and bank a account with balance {int}")
    public void aMerchantWithFirstNameLastNameCPRAndBankAAccountWithBalance(String string, String string2, String string3, int int1) {
        User user = new User();
        if (string.isBlank()) {
            invalidUser = true;
        } else if (string2.isBlank()) {
            invalidUser = true;
        } else if (string3.isBlank()) {
            invalidUser = true;
        } else if (int1 < 0) {
            invalidAmount = true;
            balance = new BigDecimal(int1);
        } else {
            user.setFirstName(string);
            user.setLastName(string2);
            user.setCprNumber(string3);
            merchant = new Merchant(new PersonalInfo(string, string2, string3), createAccountWithBalance(user, BigDecimal.valueOf(int1)));
        }
    }

    @When("the merchant is registered to DTUPay")
    public void theMerchantIsRegisteredToDTUPay() {
        mid = merchantAPI.registerMerchant(merchant);
    }

    @Given("{int} tokens and a customer with account ID {string}")
    public void tokensAndACustomerWithAccountID(int amount, String cid) {
        AccountID customerId = new AccountID(cid);
        try {
            tokens.addAll(customerAPI.createTokens(amount, customerId));
        } catch (ClientException e) {
            error = true;
            errorStatus = e.getStatus();
            errorMessage = e.getMessage();
            System.out.println("STATUS: " + errorStatus);
            System.out.println("MSG: " + errorMessage);
        }
    }

    @Given("the customer has {int} unused token")
    public void theCustomerHasUnusedToken(int amount) {
        try {
            tokens.addAll(customerAPI.createTokens(amount, cid));
        } catch (ClientException e) {
            error = true;
            errorStatus = e.getStatus();
            errorMessage = e.getMessage();
            System.out.println("STATUS: " + errorStatus);
            System.out.println("MSG: " + errorMessage);
        }
    }

    @When("the merchant initiates a payment with the token and an amount {int}")
    public void theMerchantInitiatesAPaymentWithTheTokenAndAnAmount(int amount) {
        paymentAmount = new BigDecimal(amount);
        usedToken = tokens.poll();
        try {
            merchantAPI.pay(usedToken, mid, paymentAmount);
        } catch (ClientException e) {
            error = true;
            errorStatus = e.getStatus();
            errorMessage = e.getMessage();
            System.out.println("STATUS: " + errorStatus);
            System.out.println("MSG: " + errorMessage);
        }
    }

    private boolean isNegativePaymentAmount(int amount) {
        return amount < 0;
    }

    private boolean customerOutOfTokens() {
        return tokens.isEmpty();
    }

    @Then("the merchant's balance is {int}")
    public void theMerchantSBalanceIs(int balance) {
        assertEquals(new BigDecimal(balance), getBalance(merchant.getBankNumber()));
    }

    @Then("the customer's balance is {int}")
    public void theCustomerSBalanceIs(int balance) {
        assertEquals(new BigDecimal(balance), getBalance(customer.getBankNumber()));
    }

    private BankNumber createAccountWithBalance(User user, BigDecimal balance) {
        try {
            System.out.println("Creating bank account for: ");
            System.out.println(" - For user: " + user.getFirstName() + ", " + user.getLastName() + ", " + user.getCprNumber());
            var id = bankService.createAccountWithBalance(user, balance);
            System.out.println(" - Success: " + id);
            accountIds.add(id);
            return new BankNumber(id);
        } catch (BankServiceException_Exception e) {
            // This can be uncommented if to delete a user if the After method was not ran correctly
            identifyAndDeleteBankAccount(user);
            e.printStackTrace();
            fail();
        }
        return null;
    }

    private void identifyAndDeleteBankAccount(User user) {
        System.out.println("Trying to identify bank account for user");
        for (var account : bankService.getAccounts()) {
            if (account.getUser().getCprNumber().equals(user.getCprNumber())) {
                var id = account.getAccountId();
                System.out.println("Found bank account for user: " + id);
                try {
                    bankService.retireAccount(id);
                    System.out.println("Successfully deleted user");
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }

    private BigDecimal getBalance(BankNumber banknumber) {
        try {
            return bankService.getAccount(banknumber.getIban()).getBalance();
        } catch (BankServiceException_Exception e) {
            fail();
        }
        return null;
    }

    @Given("a customer with name {string} and {int} tokens")
    public void aCustomerWithNameAndTokens(String name, int amount) {
        Customer customer2 = new Customer(new PersonalInfo(name, "lastName", "comeCpr"),
                new BankNumber("someBankNr"));
        cid2 = customerAPI.registerCustomer(customer2);
        try {
            tokens.addAll(customerAPI.createTokens(amount, cid2));
        } catch (ClientException e) {
            error = true;
            errorStatus = e.getStatus();
            errorMessage = e.getMessage();
            System.out.println("STATUS: " + errorStatus);
            System.out.println("MSG: " + errorMessage);
        }
    }

    @And("customer requests {int} tokens")
    public void customerRequestsTokens(int amount) {
        try {
            List<Token> createdTokens = customerAPI.createTokens(amount, cid2);
            tokens.addAll(createdTokens);
        } catch (ClientException e) {
            error = true;
            errorStatus = e.getStatus();
            errorMessage = e.getMessage();
            System.out.println("STATUS: " + errorStatus);
            System.out.println("MSG: " + errorMessage);
        }

    }

    // TODO
    @Then("customer has {int} tokens")
    public void customerHasTokens(int amount) {
        //assertEquals(amount, customerAPI.getTokenCount(cid2));
    }

    @Then("exception of type {string} is thrown")
    public void exceptionOfTypeIsThrown(String exceptionType) {
    }

    @Given("an unregistered customer with name {string}")
    public void anUnregisteredCustomerWithName(String name) {
        Customer customer2 = new Customer(new PersonalInfo(name, "lastName", "comeCpr"),
                new BankNumber("someBankNr"));
        cid2 = new AccountID("unregisteredID");
    }

    @And("the customer has {int} unused invalid token")
    public void theCustomerHasUnusedInvalidToken(int amount) {
        for (int i = 0; i < amount; i++) {
            invalidTokens.add(new Token("invalidToken"));
        }
        assertEquals(amount, invalidTokens.size());
    }

    @When("the merchant initiates a payment with invalid token and an amount {int}")
    public void theMerchantInitiatesAPaymentWithInvalidTokenAndAnAmount(int amount) {
        paymentAmount = new BigDecimal(amount);
        invalidCustomerToken = invalidTokens.poll();
        try {
            merchantAPI.pay(invalidCustomerToken, mid, paymentAmount);
        } catch (ClientException e) {
            error = true;
            errorStatus = e.getStatus();
            errorMessage = e.getMessage();
            System.out.println("STATUS: " + errorStatus);
            System.out.println("MSG: " + errorMessage);
        }
    }

    @When("the merchant initiates a payment with the previously used token")
    public void theMerchantInitiatesAPaymentWithThePreviouslyUsedToken() {
    }

    @When("the customer generates a report")
    public void theCustomerGeneratesAReport() {
        customerReport = customerAPI.getCustomerReport(cid);
    }

    @Then("a manager report is returned with the following amounts")
    public void aManagerReportIsReturnedWithPaymentsWithTheFollowingAmounts(List<BigDecimal> amounts) {
        assertEquals(amounts.size(), managerReport.size());
        for (var payment : managerReport) {
            assert (amounts.contains(payment.getAmount()));
        }
    }

    @Then("a customer report is returned with the following amounts")
    public void aCustomerReportIsReturnedWithPaymentsWithTheFollowingAmounts(List<BigDecimal> amounts) {
        assertEquals(amounts.size(), customerReport.size());
        for (var payment : customerReport) {
            assert (amounts.contains(payment.getAmount()));
        }
    }

    @Then("a merchant report is returned with the following amounts")
    public void aMerchantReportIsReturnedWithPaymentsWithTheFollowingAmounts(List<BigDecimal> amounts) {
        assertEquals(amounts.size(), merchantReport.size());
        for (var payment : merchantReport) {
            assert (amounts.contains(payment.getAmount()));
        }
    }

    @And("a customer with first name {string} last name {string} CPR {string} and non-existent bank account")
    public void aCustomerWithFirstNameLastNameCPRAndNonExistentBankAccount(String arg0, String arg1, String arg2) {
        User user = new User();
        user.setFirstName(arg0);
        user.setLastName(arg1);
        user.setCprNumber(arg2);
        invalidBankNr = new BankNumber("invalidBA");
        customer = new Customer(new PersonalInfo(arg0, arg1, arg2), invalidBankNr);
    }

    @Given("a merchant with invalid account id {string}")
    public void aMerchantWithInvalidAccountId(String accountId) {
        mid = new AccountID(accountId);
    }

    @Then("an empty merchant report is returned")
    public void anEmptyManagerReportIsReturned() {
        assertEquals(0, merchantReport.size());
    }

    @Then("an empty customer report is returned")
    public void anEmptyCustomerReportIsReturned() {
        assertEquals(0, customerReport.size());
    }


    @Given("a customer with account id {string}")
    public void aCustomerWithAccountId(String string) {
        cid = new AccountID(string);
    }

    @When("the merchant generates a report")
    public void theMerchantGeneratesAReport() {
        merchantReport = merchantAPI.getMerchantReport(mid);
    }

    @Given("a merchant with account id {string}")
    public void aMerchantWithAccountId(String string) {
        mid = new AccountID(string);
    }

    @When("the manager generates a report")
    public void theManagerGeneratesAReport() {
        managerReport = managerAPI.getManagerReport();
    }

    @When("the customer gets his DTUPay account")
    public void theCustomerGetsHisDTUPayAccount() {
        try {
            customer = customerAPI.getCustomer(cid);
        } catch (ClientException e) {
            error = true;
            errorStatus = e.getStatus();
            errorMessage = e.getMessage();
        }
    }

    @Then("an error occurs")
    public void anErrorOccurs() {
        assert (error);
    }

    @Then("the status is {int}")
    public void theStatusIs(Integer int1) {
        assertEquals(int1, (Integer) errorStatus);
    }

    @Then("the message is {string}")
    public void theMessageIs(String string) {
        assertEquals(string, errorMessage);
    }

    @When("the customer updates his name to {string}")
    public void theCustomerUpdatesHisNameTo(String string) {
        var newCustomer = new Customer(new PersonalInfo(string, customer.getInfo().getLastName(), customer.getInfo().getCprNumber()), customer.getBankNumber());
        customerAPI.updateCustomer(cid, newCustomer);
    }

    @Then("the customers first name is {string}")
    public void theCustomersNameIs(String string) {
        assertEquals(string, customer.getInfo().getFirstName());
    }

    @Then("no error occurs")
    public void noErrorOccurs() {
        assertFalse(error);
    }


    @Then("the customer is deleted")
    public void theCustomerIsDeleted() {
        var t = customerAPI.deleteCustomer(cid).readEntity(Boolean.class);
        assertTrue(t);
        var y = customerAPI.deleteCustomer(cid).readEntity(Boolean.class);
        assertFalse(y);
    }


    @Given("a merchant with first name {string} last name {string} CPR {string} and non-existent bank account")
    public void aMerchantWithFirstNameLastNameCPRAndNonExistentBankAccount(String name, String lastname, String cpr) {
//        for (var account : bankService.getAccounts()) {
//            System.out.println(account.getUser().getLastName());
//            if (account.getUser().getLastName() == "Lastname") {
//                var id = account.getAccountId();
//                System.out.println("Found bank account for user: " + id);
//                try {
//                    bankService.retireAccount(id);
//                    System.out.println("Successfully deleted user");
//                } catch (Exception e) {
//                    e.getMessage();
//                }
//            }
//            if (account.getUser().getLastName() == "Michael") {
//                var id = account.getAccountId();
//                System.out.println("Found bank account for user: " + id);
//                try {
//                    bankService.retireAccount(id);
//                    System.out.println("Successfully deleted user");
//                } catch (Exception e) {
//                    e.getMessage();
//                }
//            }
//        }
        User user = new User();
        user.setFirstName(name);
        user.setLastName(lastname);
        user.setCprNumber(cpr);
        invalidBankNr = new BankNumber("invalidMerchantBank");
        merchant = new Merchant(new PersonalInfo(name, lastname, cpr), invalidBankNr);
    }

    @Then("a manager report with atleast {int} payments are returned")
    public void aReportWithAtleastPaymentsAreReturned(Integer int1) {
        assert (managerReport.size() >= int1);
    }

    @After
    public void closeClients() {
        customerAPI.close();
        merchantAPI.close();
        managerAPI.close();
    }

    @After
    public void deleteAccount() {
        for (String accountId : accountIds) {
            System.out.println("Deleting bank account: " + accountId);
            try {
                bankService.retireAccount(accountId);
                System.out.println(" - Success");
            } catch (BankServiceException_Exception e) {
                e.printStackTrace();
                System.out.println(" - Account does not exit");
            }
        }
    }

}
