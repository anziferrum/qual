package ru.anziferrum.testpay.dao;

import org.hibernate.Session;
import org.json.JSONObject;
import ru.anziferrum.testpay.entity.Transactions;
import ru.anziferrum.testpay.enums.TransactionState;
import ru.anziferrum.testpay.exception.E400Exception;
import ru.anziferrum.testpay.exception.E401Exception;
import ru.anziferrum.testpay.exception.E500Exception;
import ru.anziferrum.testpay.notification.NotificationBean;
import ru.anziferrum.testpay.notification.NotificationManager;
import ru.anziferrum.testpay.util.Util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Random;

import static ru.anziferrum.testpay.util.Util.*;

/**
 * @author anziferrum
 */
public class TransactionDAO {
    private NotificationManager notificationManager = NotificationManager.getInstance();
    private AccessTokenDAO accessTokenDAO = AccessTokenDAO.getInstance();

    private static TransactionDAO instance;

    private TransactionDAO() {}

    public static TransactionDAO getInstance() {
        if (instance == null) {
            instance = new TransactionDAO();
        }

        return instance;
    }

    public String generateTransactionId() {
        return "" + System.currentTimeMillis() + new Random(System.currentTimeMillis()).nextInt(1_000_000);
    }

    public Transactions createTransaction(String json, String token) throws E400Exception, E401Exception, E500Exception {
        JSONObject parsed;

        try {
            parsed = new JSONObject(json);
        } catch (Exception e) {
            throw new E400Exception("Unable to parse incoming json!");
        }

        String id = generateTransactionId();
        Date creationDate = new Date();
        String notificationURL = parsed.getString("notification_url");
        TransactionState transactionState = TransactionState.CREATED;

        JSONObject payer = parsed.getJSONObject("payer");
        String payerEmail = payer.getString("email");

        JSONObject transaction = parsed.getJSONObject("transaction");
        String externalId = transaction.getString("external_id");
        JSONObject amount = transaction.getJSONObject("amount");
        BigDecimal value = amount.getBigDecimal("value");
        String currency = amount.getString("currency");

        Transactions result;
        try {
            Session session = hu.getSession();
            session.beginTransaction();

            result = new Transactions(
                    id,
                    value,
                    currency,
                    payerEmail,
                    creationDate,
                    externalId,
                    transactionState.name()
            );
            session.save(result);

            session.getTransaction().commit();
            session.close();

            notificationManager.enqueue(new NotificationBean(
                    currency,
                    value.setScale(2, RoundingMode.HALF_UP),
                    id,
                    externalId,
                    transactionState,
                    notificationURL,
                    Util.SHA2(
                            currency,
                            value,
                            accessTokenDAO.getMerchantByToken(token),
                            id,
                            externalId,
                            transactionState
                    )
            ));
        } catch (E500Exception e) {
            throw e;
        } catch (Exception e) {
            throw new E500Exception(e.getMessage());
        }

        return result;
    }
}