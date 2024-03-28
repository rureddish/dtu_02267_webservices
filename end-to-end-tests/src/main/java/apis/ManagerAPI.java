package apis;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import java.util.List;

import models.Payment;

public class ManagerAPI {
    Client client;
    WebTarget managerFacadeService;

    public ManagerAPI() {
        client = ClientBuilder.newClient();
        managerFacadeService = client.target("http://localhost:8084/managerAPI");
    }

    public List<Payment> getManagerReport() {
        var response = managerFacadeService
                .path("payments")
                .request()
                .get();
        return response.readEntity(new GenericType<List<Payment>>() {
        });
    }

    public void close() {
        client.close();
    }
}
