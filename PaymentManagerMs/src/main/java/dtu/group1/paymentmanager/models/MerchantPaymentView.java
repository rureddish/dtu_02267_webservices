package dtu.group1.paymentmanager.models;

import java.math.BigDecimal;

import dtu.group1.common.models.*;

import lombok.Getter;

@Getter
public class MerchantPaymentView {
    private Payment.PaymentStatus status;
    private BigDecimal amount;
    private Token token;

    public MerchantPaymentView(Payment p) {
        status = p.getStatus();
        amount = p.getAmount();
        token = p.getToken();
    }
}
