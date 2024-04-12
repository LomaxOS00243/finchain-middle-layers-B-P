package finchainstorage.businesslayer.message;


import org.springframework.http.HttpStatus;
import java.util.Map;


public class HttpResponse {
    private final HttpStatus status;
    private final String message;
    private Map<?, ?> data;

    public HttpResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
    public HttpResponse(HttpStatus status, String message, Map<?, ?> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public String toStringWithNotData() {
        return "Response: " +
                " {\n" +
                "  Status:" + status + '\n' +
                "  Message:'" + message + '\n' +
                '}';
    }
    public String toStringWithData() {
        return "Response: " +
                " {\n" +
                "  Status:" + status + '\n' +
                "  Message:'" + message + '\n' +
                "  Data:" + data + '\n' +
                '}';
    }
}
