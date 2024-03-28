package dtu.group1.paymentmanager.adapter.rest;

import dtu.group1.paymentmanager.*;
import dtu.group1.common.models.*;
import dtu.group1.paymentmanager.models.*;

import javax.ws.rs.*;
import java.util.List;


import javax.inject.Inject;

@Path("/payments")
public class PaymentsResource {

    @Inject
    IPaymentManager paymentmanager;

    @Path("/customer/{id}")
    @GET
    public List<CustomerPaymentView> getPaymentsForCustomer(@PathParam("id") AccountID customer) {
        return paymentmanager.getPaymentsForCustomer(customer);
    }

    @Path("/merchant/{id}")
    @GET
    public List<MerchantPaymentView> getPaymentsForMerchant(@PathParam("id") AccountID merchant) {
        return paymentmanager.getPaymentsForMerchant(merchant);
    }

    @GET
    public List<Payment> getPaymentsForManager() {
        return paymentmanager.getPaymentsForManager();
    }
}
