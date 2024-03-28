package dtu.group1.accountmanager;

import dtu.group1.common.models.*;
import dtu.group1.common.exceptions.*;

import java.util.List;
import java.util.Map;

public interface IAccountManager {
    // Merchant
    AccountID registerMerchant(Merchant merchant);

    List<Merchant> getAllMerchants();

    boolean deleteMerchant(AccountID mid);

    boolean updateMerchant(AccountID id, Merchant merchant);

    Merchant getMerchant(AccountID cid) throws NoSuchMerchantException;

    // Customer
    AccountID registerCustomer(Customer customer);

    List<Customer> getAllCustomers();

    void deleteCustomer(AccountID cid) throws NoSuchCustomerException;

    boolean updateCustomer(AccountID cid, Customer customer) throws NoSuchCustomerException;

    Customer getCustomer(AccountID cid) throws NoSuchCustomerException;
}
