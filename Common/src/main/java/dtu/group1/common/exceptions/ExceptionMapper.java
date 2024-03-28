package dtu.group1.common.exceptions;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {
    final Response.Status DEFAULT_STATUS = Response.Status.NOT_FOUND;

    @Override
    public Response toResponse(Exception exception) {
        // Translate jaxrs exceptions in the same way
        if (exception instanceof WebApplicationException) {
            return ((WebApplicationException) exception).getResponse();
        }
        Response.Status status = DEFAULT_STATUS;
        if (exception.getClass().isAnnotationPresent(Status.class)) {
            status = exception.getClass().getAnnotation(Status.class).value();
        }
        var builder = Response.status(status);
        builder.entity(exception.getMessage());
        return builder.build();
    }
}
