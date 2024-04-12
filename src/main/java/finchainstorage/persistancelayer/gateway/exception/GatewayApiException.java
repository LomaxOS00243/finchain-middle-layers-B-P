package finchainstorage.persistancelayer.gateway.exception;

public class GatewayApiException extends RuntimeException {

    public GatewayApiException(String message){ super(message); }
    public GatewayApiException (String message, Throwable cause) { super(message, cause); }

}
