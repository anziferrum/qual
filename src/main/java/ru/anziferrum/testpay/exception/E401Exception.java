package ru.anziferrum.testpay.exception;

import org.springframework.http.HttpStatus;

/**
 * @author anziferrum
 */
public class E401Exception extends AbstractCustomException {
    public E401Exception(String message) {
        super(message);
        this.details = message;
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String getErrorCode() {
        return "AUTHENTIFICATION_FAILURE";
    }

    @Override
    public String getErrorDescription() {
        return "Authentication failed due to invalid authentication credentials";
    }
}
