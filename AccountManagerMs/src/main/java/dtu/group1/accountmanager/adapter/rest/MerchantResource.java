package dtu.group1.accountmanager.adapter.rest;

import dtu.group1.accountmanager.*;
import dtu.group1.common.exceptions.NoSuchMerchantException;
import dtu.group1.common.models.*;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.inject.Inject;
import javax.ws.rs.core.Response;

@Path("/merchants")
public class MerchantResource {

    @Inject
    IAccountManager accountManager;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public AccountID registerMerchant(Merchant merchant) {
        return accountManager.registerMerchant(merchant);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public boolean updateMerchant(@PathParam("id") AccountID mid, Merchant merchant) {
        return accountManager.updateMerchant(mid, merchant);
    }

    @GET
    @Path("{id}")
    public Response getMerchant(@PathParam("id") AccountID mid) {
        try {
            return Response.ok().entity(accountManager.getMerchant(mid)).build();
        } catch (NoSuchMerchantException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }

    }

    @DELETE
    @Path("/{id}")
    public boolean deleteMerchant(@PathParam("id") AccountID mid) {
        return accountManager.deleteMerchant(mid);
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllMerchants() {
        return Response.ok().entity(accountManager.getAllMerchants()).build();
    }
}
