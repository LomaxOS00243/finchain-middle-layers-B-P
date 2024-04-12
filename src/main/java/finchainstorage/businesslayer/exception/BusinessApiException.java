package finchainstorage.businesslayer.exception;

public class BusinessApiException extends RuntimeException {

    public BusinessApiException(String message) { super(message); }
    public BusinessApiException(String message, Throwable cause) { super(message, cause); }
}
