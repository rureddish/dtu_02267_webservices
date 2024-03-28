package dtu.group1.accountmanager.adapter.rest;

import dtu.group1.accountmanager.*;
import dtu.group1.common.models.*;
import dtu.group1.common.exceptions.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Path("/customers")
public class CustomerResource {

    @Inject
    IAccountManager accountManager;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public AccountID registerCustomer(Customer customer) {
        return accountManager.registerCustomer(customer);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{id}")
    public boolean updateCustomer(@PathParam("id") AccountID cid, Customer customer) throws NoSuchCustomerException {
        return accountManager.updateCustomer(cid, customer);
    }

    @Path("/{id}")
    @GET
    public Response getCustomer(@PathParam("id") AccountID customer) {
        try {
            return Response.ok().entity(accountManager.getCustomer(customer)).build();
        } catch (NoSuchCustomerException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllCustomers() {
        return Response.ok().entity(accountManager.getAllCustomers()).build();
    }

    @DELETE
    @Path("{id}")
    public boolean deleteCustomer(@PathParam("id") AccountID cid) {
        try {
            accountManager.deleteCustomer(cid);
            return true;
        }
        catch(NoSuchCustomerException e) {
            return false;
        }
    }
}
