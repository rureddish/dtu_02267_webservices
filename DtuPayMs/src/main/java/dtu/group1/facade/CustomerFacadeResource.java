package dtu.group1.facade;

import dtu.group1.common.exceptions.InvalidTokenCreationRequestException;
import dtu.group1.common.models.*;
import dtu.group1.facade.models.*;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;

@Path("/customerAPI")
public class CustomerFacadeResource {

    @Inject
    CustomerFacadeAPI customerFacadeAPI;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/customers")
    public Response registerCustomer(Customer customer) {
        return customerFacadeAPI.registerCustomer(customer);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("customers/{id}")
    public Response getCustomer(@PathParam("id") AccountID cid) {
        return customerFacadeAPI.getCustomer(cid);
    }

    @DELETE
    @Path("customers/{id}")
    public Response deleteCustomer(@PathParam("id") AccountID cid) {
        System.out.println("delete customer facaderesource");
        return customerFacadeAPI.deleteCustomer(cid);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/customers")
    public Response getAllCustomer() {
        return customerFacadeAPI.getAllCustomers();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/customers/{id}")
    public Response updateCustomer(@PathParam("id") AccountID cid, Customer customer) {
        return customerFacadeAPI.updateCustomer(cid, customer);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/tokens")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Token> createTokens(TokenRequest req) throws InvalidTokenCreationRequestException {
        return customerFacadeAPI.createTokens(req.getAmount(), req.getAccountID());
    }

    @GET
    @Path("/payments/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response customerReport(@PathParam("id") AccountID cid) {
        return customerFacadeAPI.customerReport(cid);
    }
}

