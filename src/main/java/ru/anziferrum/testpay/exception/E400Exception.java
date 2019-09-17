package ru.anziferrum.testpay.exception;

import org.springframework.http.HttpStatus;

/**
 * @author anziferrum
 */
public class E400Exception extends AbstractCustomException {
    public E400Exception(String message) {
        super(message);
        this.details = message;
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return "INVALID_REQUEST";
    }

    @Override
    public String getErrorDescription() {
        return "Request is not well-formatted, syntactically incorrect or violates schema";
    }
}
