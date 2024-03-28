package dtu.group1.facade;

import javax.inject.Inject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@Path("/managerAPI")
public class ManagerFacadeResource {

    @Inject
    ManagerFacadeAPI managerFacadeAPI;

    @GET
    @Path("/payments")
    public Response managerReport() {
        return managerFacadeAPI.managerReport();
    }
}
