package finchainstorage.businesslayer.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.util.Map;


public class HttpResponse {
    private final HttpStatus status;
    private final String message;
    private final Map<?, ?> data;

    public HttpResponse(HttpStatus status, String message, Map<?, ?> data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    @Override
    public String toString() {
        return "Response: " +
                " {\n" +
                "  Status:" + status + '\n' +
                "  Message:'" + message + '\n' +
                "  Data:" + data + '\n' +
                '}';
    }
}
