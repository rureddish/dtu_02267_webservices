package dtu.group1.facade;

import dtu.group1.common.models.AccountID;
import dtu.group1.common.models.Merchant;
import dtu.group1.common.models.PaymentRequest;
import dtu.group1.common.exceptions.*;

import javax.inject.Inject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//TODO need tests
@Path("/merchantAPI")
public class MerchantFacadeResource {

    @Inject
    MerchantFacadeAPI merchantFacadeAPI;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/merchants/{id}")
    public Response getMerchant(@PathParam("id") AccountID mid) {
        return merchantFacadeAPI.getMerchant(mid);
    }

    @DELETE
    @Path("/merchants/{id}")
    public Response deleteMerchant(@PathParam("id") AccountID mid) {
        return merchantFacadeAPI.deleteMerchant(mid);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/merchants/{id}")
    public Response updateMerchant(@PathParam("id") AccountID mid, Merchant merchant) {
        return merchantFacadeAPI.updateMerchant(mid, merchant);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/merchants")
    public AccountID registerMerchant(Merchant merchant) {
        try {
            return merchantFacadeAPI.registerMerchant(merchant);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/payments")
    public boolean pay(PaymentRequest req) throws BankException, InvalidTokenException {
        return merchantFacadeAPI.pay(req);
    }

    @GET
    @Path("/payments/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response merchantReport(@PathParam("id") AccountID mid) {
        return merchantFacadeAPI.merchantReport(mid);
    }
}
