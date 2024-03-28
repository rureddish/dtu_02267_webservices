package dtu.group1.paymentmanager.models;

import dtu.group1.common.events.*;
import dtu.group1.common.models.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.function.BiConsumer;

import lombok.Getter;

@Getter
public class Payment {
    @Getter
    public enum PaymentStatus {
        PENDING("Pending"),
        SUCCEEDED("Succeeded"),
        FAILED("Failed");
        private final String status;

        private PaymentStatus(String s) {
            status = s;
        }
    }

    private PaymentStatus status;
    private BigDecimal amount;
    private AccountID customer;
    private Token token;
    private BankNumber customerBankNumber;
    private AccountID merchant;
    private BankNumber merchantBankNumber;

    static private Map<Class<? extends Event>, BiConsumer<Payment, Event>> handlers = new HashMap<>();

    static public Set<Class<? extends Event>> getHandlers() {
        return handlers.keySet();
    }

    static {
        handlers.put(PaymentRequestedEvent.class, (self, e) -> self.apply((PaymentRequestedEvent) e));
        handlers.put(TokenValidatedForCustomerEvent.class, (self, e) -> self.apply((TokenValidatedForCustomerEvent) e));
        handlers.put(CustomerBankNumberAssignedToPaymentEvent.class, (self, e) -> self.apply((CustomerBankNumberAssignedToPaymentEvent) e));
        handlers.put(MerchantBankNumberAssignedToPaymentEvent.class, (self, e) -> self.apply((MerchantBankNumberAssignedToPaymentEvent) e));
        handlers.put(PaymentSucceededEvent.class, (self, e) -> self.apply((PaymentSucceededEvent) e));
        handlers.put(PaymentFailedEvent.class, (self, e) -> self.apply((PaymentFailedEvent) e));
        handlers.put(TokenNotValidatedForCustomerEvent.class, (self, e) -> self.apply((TokenNotValidatedForCustomerEvent) e));
    }

    public Payment() {
        status = PaymentStatus.PENDING;
    }

    public boolean readyForTransaction() {
        return customerBankNumber != null
                && merchantBankNumber != null
                && amount != null
                && status == PaymentStatus.PENDING;
    }

    public void applyEvent(Event event) {
        handlers.getOrDefault(event.getClass(), (self, e) -> self.missingHandle(e)).accept(this, event);
    }

    public void applyAllEvents(List<Event> events) {
        for (var e : events) {
            applyEvent(e);
        }
    }

    private void missingHandle(Event e) {
        System.out.println("Tried to apply event without an appropiate method: " + e.getClass());
    }

    private void apply(PaymentRequestedEvent e) {
        merchant = e.getPaymentRequest().getMid();
        token = e.getPaymentRequest().getToken();
        amount = e.getPaymentRequest().getAmount();
    }

    private void apply(TokenValidatedForCustomerEvent e) {
        customer = e.getAccountID();
    }

    private void apply(CustomerBankNumberAssignedToPaymentEvent e) {
        customerBankNumber = e.getBankNumber();
    }

    private void apply(MerchantBankNumberAssignedToPaymentEvent e) {
        merchantBankNumber = e.getBankNumber();
    }

    private void apply(PaymentSucceededEvent e) {
        status = PaymentStatus.SUCCEEDED;
    }

    private void apply(PaymentFailedEvent e) {
        status = PaymentStatus.FAILED;
    }

    private void apply(TokenNotValidatedForCustomerEvent e) {
        status = PaymentStatus.FAILED;
    }
}
