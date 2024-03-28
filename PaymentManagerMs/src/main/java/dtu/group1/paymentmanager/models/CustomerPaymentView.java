package dtu.group1.paymentmanager.models;

import java.math.BigDecimal;

import dtu.group1.common.models.*;

import lombok.Getter;

@Getter
public class CustomerPaymentView {
    private Payment.PaymentStatus status;
    private BigDecimal amount;
    private Token token;
    private AccountID merchant;

    public CustomerPaymentView(Payment p) {
        status = p.getStatus();
        amount = p.getAmount();
        token = p.getToken();
        merchant = p.getMerchant();
    }
}
