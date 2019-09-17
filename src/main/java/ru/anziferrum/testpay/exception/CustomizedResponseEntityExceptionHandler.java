package ru.anziferrum.testpay.exception;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * @author anziferrum
 */
@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler.class);

    @ExceptionHandler( { AbstractCustomException.class } )
    public final ResponseEntity<String> handleException(AbstractCustomException ex) {
        return new ResponseEntity<>(details(ex), ex.getStatusCode());
    }

    private String details(AbstractCustomException ex) {
        LOGGER.error(ex.getDetails());

        JSONObject result = new JSONObject().
            put("error", ex.getErrorCode()).
            put("error_description", ex.getErrorDescription());

        return result.toString();
    }

}
