package models;

import dtu.group1.common.models.*;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement // Needed for XML serialization and deserialization
@Data // Automatic getter and setters and equals etc
@NoArgsConstructor // Needed for JSON deserialization and XML serialization and deserialization
@AllArgsConstructor
public class Payment {
    private String status;
    private Boolean successful;
    private BigDecimal amount;
    private AccountID customer;
    private Token token;
    private BankNumber customerBankNumber;
    private AccountID merchant;
    private BankNumber merchantBankNumber;
}
