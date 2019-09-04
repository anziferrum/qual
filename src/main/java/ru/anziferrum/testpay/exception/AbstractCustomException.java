package ru.anziferrum.testpay.exception;

import org.springframework.http.HttpStatus;

/**
 * @author anziferrum
 *
 * This was created to provide more clear error messages in logs
 */
public abstract class AbstractCustomException extends Exception {
    public AbstractCustomException(String message) {
        super(message);
    }

    abstract HttpStatus getStatusCode();

    abstract String getErrorCode();

    abstract String getErrorDescription();

    abstract String getDetails();
}
