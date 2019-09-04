package ru.anziferrum.testpay.exception;

import org.springframework.http.HttpStatus;

/**
 * @author anziferrum
 */
public class E500Exception extends AbstractCustomException {
    private String details;

    public E500Exception(String message) {
        super(message);
        this.details = message;
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }

    @Override
    public String getErrorCode() {
        return "INTERNAL_SERVER_ERROR";
    }

    @Override
    public String getDetails() {
        return details;
    }

    @Override
    public String getErrorDescription() {
        return "An internal server error has occurred";
    }
}
