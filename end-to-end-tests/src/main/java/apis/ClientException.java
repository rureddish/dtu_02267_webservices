package apis;

public class ClientException extends Exception {
    private int status;
    public ClientException(String msg, int status) {
        super(msg);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
