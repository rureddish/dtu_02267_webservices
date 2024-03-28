package dtu.group1.common.exceptions;

import javax.ws.rs.core.Response;

@Status(Response.Status.BAD_REQUEST)
public class BankException extends Exception {
    public BankException(String msg) {
        super("Bank communication fail: " + msg);
    }
}