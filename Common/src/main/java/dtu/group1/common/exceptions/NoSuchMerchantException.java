package dtu.group1.common.exceptions;

import javax.ws.rs.core.Response;

@Status(Response.Status.NOT_FOUND)
public class NoSuchMerchantException extends Exception {
    public NoSuchMerchantException() {
        super("No merchant associated with id");
    }
}