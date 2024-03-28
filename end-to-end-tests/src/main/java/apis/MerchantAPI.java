package apis;

import dtu.group1.common.models.*;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.List;

import models.MerchantPaymentView;

public class MerchantAPI {
    Client client;
    WebTarget merchantFacadeService;

    public MerchantAPI() {
        client = ClientBuilder.newClient();
        merchantFacadeService = client.target("http://localhost:8084/merchantAPI");
    }

    public AccountID registerMerchant(Merchant merchant) {
        return merchantFacadeService
                .path("/merchants")
                .request()
                .post(Entity.entity(merchant, MediaType.APPLICATION_JSON_TYPE), AccountID.class);

    }

    public void updateMerchant(AccountID mid, Merchant merchant) {
        merchantFacadeService
                .path("/merchants")
                .path(mid.getIdentifier())
                .request()
                .put(Entity.entity(merchant, MediaType.APPLICATION_JSON_TYPE));
    }

    public Merchant getMerchant(AccountID mid) throws ClientException {
        var response = merchantFacadeService
                .path("merchants")
                .path(mid.getIdentifier())
                .request()
                .get();

        if (response.getStatus() != 200) {
            throw new ClientException(response.readEntity(String.class), response.getStatus());
        }

        return response.readEntity(Merchant.class);
    }

    public Response deleteMerchant(AccountID mid) {
        return merchantFacadeService
                .path("merchants")
                .path(mid.getIdentifier())
                .request()
                .delete();
    }

    public Response pay(Token token, AccountID mid, BigDecimal amount) throws ClientException {
        var response = merchantFacadeService.path("/payments")
                .request()
                .post(Entity.entity(new PaymentRequest(token, mid, amount), MediaType.APPLICATION_JSON_TYPE));

        if (response.getStatus() != 200) {
            throw new ClientException(response.readEntity(String.class), response.getStatus());
        }

        return response;
    }

    public List<MerchantPaymentView> getMerchantReport(AccountID accountID) {
        var response = merchantFacadeService
                .path("payments")
                .path(accountID.getIdentifier())
                .request()
                .get();

        return response.readEntity(new GenericType<List<MerchantPaymentView>>() {
        });
    }

    public void close() {
        client.close();
    }
}
