package ru.anziferrum.testpay.controller;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.anziferrum.testpay.dao.AccessTokenDAO;
import ru.anziferrum.testpay.exception.AbstractCustomException;
import ru.anziferrum.testpay.exception.E400Exception;
import ru.anziferrum.testpay.security.HeaderChecker;
import ru.anziferrum.testpay.util.Util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author anziferrum
 */
@Controller
public class AccessTokenController {
    private AccessTokenDAO accessTokenDAO = AccessTokenDAO.getInstance();

    private String[] credentials;

    @RequestMapping(
            value = "/oauth2/token",
            method = RequestMethod.POST
            /*
                well, we can put headers check here as well
                like this:
                headers = { "Accept=application/json", "Accept-Language=en_US" }

                the problem is that if you specify header you want to get and there's no corresponding header in the request,
                you'll get no error message from Spring
                or maybe there's a way to handle missing/incorrect headers with error message, but I don't know how

                probably this type of fine control can be achieved via spring-boot-security

                currently this is implemented by HeaderChecker class
            */
    )
    @ResponseBody
    public ResponseEntity<?> getAccessToken(
            @RequestBody String requestBody,
            HttpServletRequest request
    ) throws AbstractCustomException {
        checkHeadersAndBody(requestBody, request);

        String token = accessTokenDAO.getAccessToken(credentials);

        JSONObject resp = new JSONObject().
            put("scope", "https://api.testpay.com/payments/*").
            put("Access-Token", token).
            put("token_type", "Bearer").
            put("expires_in", Util.TOKEN_EXPIRATION_TIME);

        return new ResponseEntity<Object>(resp.toString(), HttpStatus.OK);
    }

    private void checkHeadersAndBody(String requestBody, HttpServletRequest request) throws AbstractCustomException {
        credentials = new HeaderChecker(request).
                isHeaderPresent("Accept").hasValue("application/json").
                isHeaderPresent("Accept-Language").hasValue("en_US").
                isHeaderPresent("Authorization").startsWith("Basic").extractCredentials(true, ":").
                getCredentials();

        if (requestBody == null || requestBody.isEmpty() || !"grant_type=client_credentials".equals(requestBody)) {
            throw new E400Exception("Request body either empty or \"grant_type\" field has other value than \"client_credentials\"!");
        }
    }

}