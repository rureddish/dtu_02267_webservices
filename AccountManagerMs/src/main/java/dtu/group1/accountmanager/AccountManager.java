package dtu.group1.accountmanager;

import dtu.group1.common.models.*;
import dtu.group1.common.exceptions.*;
import dtu.group1.accountmanager.repositories.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import javax.inject.Singleton;
import javax.inject.Inject;

import java.util.stream.Collectors;

import messaging.MessageQueue;

@Singleton
public class AccountManager implements IAccountManager {
    Set<AccountID> usedIDs = ConcurrentHashMap.newKeySet();
    Map<AccountID, Merchant> registeredMerchants = new HashMap<>();

    CustomerReadRepository customerReadRepo;
    CustomerRepository customerRepo;

    @Inject public AccountManager(MessageQueue q) {
        customerReadRepo = new CustomerReadRepository(q);
        customerRepo  = new CustomerRepository(q);
    }

    @Override
    public Customer getCustomer(AccountID cid) throws NoSuchCustomerException {
        return customerReadRepo.getCustomer(cid);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerReadRepo.getCustomers();
    }

    @Override
    public void deleteCustomer(AccountID cid) throws NoSuchCustomerException {
        var customer = customerRepo.getCustomer(cid);
        customer.delete();
        customerRepo.save(customer);
    }

    @Override
    public boolean updateCustomer(AccountID id, Customer customer) throws NoSuchCustomerException {
        var newCustomer = customerRepo.getCustomer(id);
        newCustomer.update(customer.getInfo(), customer.getBankNumber());
        customerRepo.save(newCustomer);
        return true;
    }

    @Override
    public AccountID registerCustomer(Customer customer) {
        var newCustomer = dtu.group1.accountmanager.models.Customer.create(customer.getInfo(), customer.getBankNumber());
        customerRepo.save(newCustomer);
        return newCustomer.getAccountID();
    }


    @Override
    public Merchant getMerchant(AccountID mid) {
        return registeredMerchants.get(mid);
    }

    @Override
    public List<Merchant> getAllMerchants() {
        return registeredMerchants.values()
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteMerchant(AccountID mid) {
        var result = registeredMerchants.remove(mid);
        return result != null;
    }

    @Override
    public boolean updateMerchant(AccountID id, Merchant merchant) {
        return registeredMerchants.computeIfPresent(id, (k, v) -> merchant) != null;
    }

    @Override
    public AccountID registerMerchant(Merchant merchant) {
        var mid = generateUniqueID();
        registeredMerchants.put(mid, merchant);
        return mid;
    }

    private AccountID generateUniqueID() {
        AccountID id;
        do {
            id = new AccountID(UUID.randomUUID().toString());
        } while (!usedIDs.add(id));
        return id;
    }
}
