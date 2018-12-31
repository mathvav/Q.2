package net.karlshaffer.q.exceptions;

import org.springframework.http.HttpStatus;

public class WebApplicationException extends RuntimeException {

    private HttpStatus status;
    private String message;

    public WebApplicationException(String message, HttpStatus status, Exception inner) {
        super(message, inner);
        this.status = status;
        this.message = message;
    }

    public WebApplicationException() {
        this("An error occurred.", HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    public WebApplicationException(String message, Exception inner) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR, inner);
    }

    public WebApplicationException(String message) {
        this(message, HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    public WebApplicationException(String message, HttpStatus status) {
        this(message, status, null);
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
