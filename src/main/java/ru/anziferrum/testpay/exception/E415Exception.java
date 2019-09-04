package ru.anziferrum.testpay.exception;

import org.springframework.http.HttpStatus;

/**
 * @author anziferrum
 */
public class E415Exception extends AbstractCustomException {
    private String details;

    public E415Exception(String message) {
        super(message);
        this.details = message;
    }

    @Override
    public HttpStatus getStatusCode() {
        return HttpStatus.UNSUPPORTED_MEDIA_TYPE;
    }

    @Override
    public String getErrorCode() {
        return "UNSUPPORTED_MEDIA_TYPE";
    }

    @Override
    public String getDetails() {
        return details;
    }

    @Override
    public String getErrorDescription() {
        return "The server does not support the request payload media type";
    }
}
