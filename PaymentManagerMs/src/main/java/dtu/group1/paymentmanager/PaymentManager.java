package dtu.group1.paymentmanager;

import dtu.group1.common.CorrelationMap;
import dtu.group1.common.models.*;
import dtu.group1.common.events.*;
import dtu.group1.common.*;
import dtu.group1.common.exceptions.BankException;
import dtu.ws.fastmoney.BankService;
import dtu.ws.fastmoney.BankServiceException_Exception;
import messaging.MessageQueue;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.quarkus.runtime.Startup;

import java.math.BigDecimal;

import java.util.List;
import java.util.stream.Collectors;

import dtu.group1.paymentmanager.models.*;
import dtu.group1.paymentmanager.repositories.*;

@Singleton
@Startup
public class PaymentManager implements IPaymentManager {
    private BankService bank;
    private MessageQueue queue;

    private ReadModelRepository repo;
    private CorrelationMap<PaymentRequest> paymentRequests = new CorrelationMap(PaymentManager.class.toString());

    @Inject
    public PaymentManager(MessageQueue q, ReadModelRepository r, BankService b) {
        bank = b;
        queue = q;
        repo = r;
        repo.addTransactionHandle(this::onPaymentReadyForTransaction);
    }

    public void onPaymentReadyForTransaction(CorrelationID corrid, Payment payment) {
        try {
            var result = makeBankTransaction(payment.getCustomerBankNumber(), payment.getMerchantBankNumber(), payment.getAmount());
            queue.publish(new PaymentSucceededEvent(corrid));
        } catch (BankException e) {
            queue.publish(new PaymentFailedEvent(e, corrid));
        }
    }

    @Override
    public List<CustomerPaymentView> getPaymentsForCustomer(AccountID cid) {
        return repo.getPayments().stream()
                .filter(p -> cid.equals(p.getCustomer()))
                .map(p -> new CustomerPaymentView(p))
                .collect(Collectors.toList());
    }

    @Override
    public List<MerchantPaymentView> getPaymentsForMerchant(AccountID mid) {
        return repo.getPayments().stream()
                .filter(p -> mid.equals(p.getMerchant()))
                .map(p -> new MerchantPaymentView(p))
                .collect(Collectors.toList());
    }

    @Override
    public List<Payment> getPaymentsForManager() {
        return repo.getPayments().stream().collect(Collectors.toList());
    }

    private boolean makeBankTransaction(BankNumber debtor, BankNumber creditor, BigDecimal amount) throws BankException {
        try {
            bank.transferMoneyFromTo(debtor.getIban(), creditor.getIban(), amount, "test description");
        } catch (BankServiceException_Exception e) {
            throw new BankException(e.getMessage());
        }
        return true;
    }
}
