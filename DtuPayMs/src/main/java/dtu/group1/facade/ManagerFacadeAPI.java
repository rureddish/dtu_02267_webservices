package dtu.group1.facade;


import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

@Singleton
public class ManagerFacadeAPI {
    WebTarget reportService;

    public ManagerFacadeAPI() {
        Client client = ClientBuilder.newClient();
        reportService = client.target("http://payment-manager-ms:8080/payments");
    }

    public Response managerReport() {
        return reportService.request().get();
    }
}
