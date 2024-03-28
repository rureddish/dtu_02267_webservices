package dtu.group1.paymentmanager;

import dtu.group1.common.models.*;

import java.util.List;

import dtu.group1.paymentmanager.models.*;

public interface IPaymentManager {
    public List<CustomerPaymentView> getPaymentsForCustomer(AccountID cid);

    public List<MerchantPaymentView> getPaymentsForMerchant(AccountID mid);

    public List<Payment> getPaymentsForManager();
}
