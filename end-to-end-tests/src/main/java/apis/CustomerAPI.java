package apis;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import models.*;
import dtu.group1.common.models.*;

public class CustomerAPI {
    Client client;
    WebTarget customerFacadeService;

    public CustomerAPI() {
        client = ClientBuilder.newClient();
        customerFacadeService = client.target("http://localhost:8084/customerAPI");
    }

    public AccountID registerCustomer(Customer customer) {
        return customerFacadeService
                .path("/customers")
                .request()
                .post(Entity.entity(customer, MediaType.APPLICATION_JSON_TYPE), AccountID.class);
    }

    public void updateCustomer(AccountID cid, Customer customer) {
        customerFacadeService
                .path("/customers")
                .path(cid.getIdentifier())
                .request()
                .put(Entity.entity(customer, MediaType.APPLICATION_JSON_TYPE));
    }

    public Customer getCustomer(AccountID cid) throws ClientException {
        var response = customerFacadeService
                .path("customers")
                .path(cid.getIdentifier())
                .request()
                .get();

        if (response.getStatus() != 200) {
            throw new ClientException(response.readEntity(String.class), response.getStatus());
        }

        return response.readEntity(Customer.class);
    }

    public Response deleteCustomer(AccountID cid) {
        System.out.println("deletecustomer end to end test");
        return customerFacadeService
                .path("customers")
                .path(cid.getIdentifier())
                .request()
                .delete();
    }

    public List<Token> createTokens(int amount, AccountID cid) throws ClientException {
        TokenRequest req = new TokenRequest(amount, cid);
        var response = customerFacadeService
                .path("/tokens")
                .request()
                .post(Entity.entity(req, MediaType.APPLICATION_JSON_TYPE));

        if (response.getStatus() != 200) {
            throw new ClientException(response.readEntity(String.class), response.getStatus());
        }

        return response.readEntity(new GenericType<List<Token>>() {
        });
    }

    public List<CustomerPaymentView> getCustomerReport(AccountID accountID) {
        var response = customerFacadeService
                .path("payments")
                .path(accountID.getIdentifier())
                .request()
                .get();

        return response.readEntity(new GenericType<List<CustomerPaymentView>>() {
        });
    }

    public void close() {
        client.close();
    }
}
