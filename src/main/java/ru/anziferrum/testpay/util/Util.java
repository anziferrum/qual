package ru.anziferrum.testpay.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import ru.anziferrum.testpay.enums.TransactionState;

import javax.xml.bind.DatatypeConverter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author anziferrum
 */
public class Util {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Util.class);

    public static final int TOKEN_EXPIRATION_TIME = 60 * 60 * 3; //3 hours
    public static final int ATTEMPTS = 25;
    public static final int NOTIFICATION_PERIOD = 10_368_000; //"25 times over the course of three days"

    public static HibernateUtil hu = HibernateUtil.getInstance();

    public static String md5(String data) {
        byte[] digest;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes());
            digest = md.digest();
        } catch (NoSuchAlgorithmException e) {
            //well, probably we will never get here
            LOGGER.error("No suitable algorithm found!", e);
            digest = new byte[0];
        }

        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }

    public static String SHA2(
            String currency,
            BigDecimal amount,
            String secret,
            String id,
            String externalId,
            TransactionState transactionState
    ) {
        return DigestUtils.sha256Hex(
                currency +
                amount.setScale(2, RoundingMode.HALF_UP) +
                DigestUtils.sha256Hex(secret).toUpperCase() +
                id +
                externalId +
                transactionState.name()
        );
    }
}
