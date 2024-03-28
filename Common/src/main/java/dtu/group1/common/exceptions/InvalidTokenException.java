package dtu.group1.common.exceptions;

import dtu.group1.common.models.Token;

import javax.ws.rs.core.Response;

@Status(Response.Status.UNAUTHORIZED)
public class InvalidTokenException extends Exception {
    public InvalidTokenException(Token token) {
        super(String.format("Token %s not associated with a customer in DTU Pay", token.getToken()));
    }
}