package dtu.group1.common.exceptions;

import javax.ws.rs.core.Response;

@Status(Response.Status.NOT_FOUND)
public class NoSuchCustomerException extends Exception {
    public NoSuchCustomerException() {
        super("No customer associated with id");
    }
}
