package dtu.group1.common.exceptions;

import javax.ws.rs.core.Response;

@Status(Response.Status.FORBIDDEN)
public class InvalidTokenCreationRequestException extends Exception {
    public InvalidTokenCreationRequestException() {
        super("Customer already has more than one token");
    }
}