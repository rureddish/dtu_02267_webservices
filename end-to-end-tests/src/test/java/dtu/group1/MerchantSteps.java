/*
package dtu.group1;

import apis.ClientException;
import apis.MerchantAPI;
import dtu.group1.common.models.AccountID;
import dtu.group1.common.models.BankNumber;
import dtu.group1.common.models.Customer;
import dtu.group1.common.models.Merchant;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import dtu.ws.fastmoney.BankServiceService;
import dtu.ws.fastmoney.User;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MerchantSteps {
    MerchantAPI merchantAPI = new MerchantAPI();
    private Boolean invalidUser = false;
    private Boolean invalidAmount = false;
    private BigDecimal balance;
    Merchant merchant;
    AccountID mid;
    private boolean error = false;
    private int errorStatus;
    private String errorMessage;
    BankService bankService = new BankServiceService().getBankServicePort();
    private List<String> accountIds = new ArrayList<>();




    @Given("merchant with first name {string} last name {string} CPR {string} and bank a account with balance {int}")
    public void merchantWithFirstNameLastNameCPRAndBankAAccountWithBalance(
            String arg0, String arg1, String arg2, int arg3) {
        User user = new User();
        if (arg0.isBlank()) {
            invalidUser = true;
        } else if (arg1.isBlank()) {
            invalidUser = true;
        } else if (arg2.isBlank()) {
            invalidUser = true;
        } else if (arg3 < 0) {
            invalidAmount = true;
            balance = new BigDecimal(arg3);
        } else {
            user.setFirstName(arg0);
            user.setLastName(arg1);
            user.setCprNumber(arg2);
            merchant = new Merchant(arg0, arg1, arg2, createAccountWithBalance(user, BigDecimal.valueOf(arg3)));
        }
    }

    @And("the merchant is registered")
    public void theMerchantIsRegistered() {
        mid = merchantAPI.registerMerchant(merchant);
    }

    @When("the merchant gets his account")
    public void theMerchantGetsHisAccount() {
        try {
            merchant = merchantAPI.getMerchant(mid);
        } catch (ClientException e) {
            error = true;
            errorStatus = e.getStatus();
            errorMessage = e.getMessage();
        }
    }

    @Then("no error occur")
    public void noErrorOccur() {
        assertFalse(error);

    }

    @When("the merchant updates his name to {string}")
    public void theMerchantUpdatesHisNameTo(String arg0) {
        try {
            merchant = merchantAPI.getMerchant(mid);
        } catch (ClientException e) {
            error = true;
            errorStatus = e.getStatus();
            errorMessage = e.getMessage();
        }

        var newMerchant = new Merchant(arg0, merchant.getLastName(), merchant.getCprNumber(), merchant.getBankNumber());
        merchantAPI.updateMerchant(mid, newMerchant);
    }



    @Then("the merchant first name is {string}")
    public void theMerchantFirstNameIs(String arg0) {
        assertEquals(arg0, merchant.getFirstName());
    }

    @Then("merchant is deleted")
    public void merchantIsDeleted() {
        var t = merchantAPI.deleteMerchant(mid).readEntity(Boolean.class);
        assertTrue(t);
        var y = merchantAPI.deleteMerchant(mid).readEntity(Boolean.class);
        assertFalse(y);
    }

    @Given("merchant with account id {string}")
    public void merchantWithAccountId(String arg0) {
        mid = new AccountID(arg0);
    }

    @Then("an error occur")
    public void anErrorOccur() {
        assertTrue(error);
    }

    @And("status is {int}")
    public void statusIs(Integer int1) {
        assertEquals(int1, (Integer) errorStatus);
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


    @After
    public void closeClients() {
        merchantAPI.close();
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
*/
