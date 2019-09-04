package ru.anziferrum.testpay.controller;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.anziferrum.testpay.dao.AccessTokenDAO;
import ru.anziferrum.testpay.dao.TransactionDAO;
import ru.anziferrum.testpay.entity.Transactions;
import ru.anziferrum.testpay.exception.AbstractCustomException;
import ru.anziferrum.testpay.exception.E400Exception;
import ru.anziferrum.testpay.security.HeaderChecker;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;

/**
 * @author anziferrum
 */
@Controller
public class PaymentController {
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private TransactionDAO transactionDAO = TransactionDAO.getInstance();
    private AccessTokenDAO accessTokenDAO = AccessTokenDAO.getInstance();
    private String token;

    @RequestMapping(
            value = "/payments/payment",
            method = RequestMethod.POST
            //see AccessTokenController for explanation
    )
    @ResponseBody
    //creates a payment
    public ResponseEntity<?> payment(@RequestBody String requestBody, HttpServletRequest request) throws AbstractCustomException {
        checkHeaders(request);

        try {
            JSONObject rawSchema = new JSONObject(new JSONTokener(PaymentController.class.getResourceAsStream("/schema.json")));
            Schema schema = SchemaLoader.load(rawSchema);
            schema.validate(new JSONObject(requestBody));
        } catch (Exception e) {
            throw new E400Exception(e.getMessage());
        }

        Transactions t = transactionDAO.createTransaction(requestBody, token);

        JSONObject response = new JSONObject().
            put("id", t.getId()).
            put("create_time", sdf.format(t.getCreationDate())).
            put("state", t.getState().toLowerCase());

        return new ResponseEntity<Object>(response.toString(), HttpStatus.OK);
    }

    private void checkHeaders(HttpServletRequest request) throws AbstractCustomException {
        HeaderChecker checker = new HeaderChecker(request);

        checker.
                isHeaderPresent("Content-Type").hasValue("application/json").
                isHeaderPresent("Authorization").startsWith("Bearer").extractCredentials(false, " ");

        if (!accessTokenDAO.checkToken(checker.getCredentials()[1])) {
            throw new E400Exception("This token has expired, request a new one!");
        }

        token = checker.getCredentials()[1];
    }
}
