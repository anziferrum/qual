package ru.anziferrum.testpay.dao;

import org.hibernate.Session;
import ru.anziferrum.testpay.entity.Merchants;
import ru.anziferrum.testpay.exception.E401Exception;
import ru.anziferrum.testpay.exception.E500Exception;

import java.util.List;

import static ru.anziferrum.testpay.util.Util.*;

/**
 * @author anziferrum
 */
public class MerchantDAO {
    private static MerchantDAO instance;

    private MerchantDAO() {}

    public static MerchantDAO getInstance() {
        if (instance == null) {
            instance = new MerchantDAO();
        }

        return instance;
    }

    public boolean checkMerchant(String name, String password) throws E401Exception, E500Exception {
        List<Merchants> queryResult;
        try {
            Session session = hu.getSession();
            session.beginTransaction();

            queryResult = (List<Merchants>) session.createQuery("FROM Merchants WHERE name = :name AND password = :password").
                setParameter("name", name).
                setParameter("password", md5(password)).
                list();

            session.getTransaction().commit();
            session.close();
        } catch (E500Exception e) {
            throw e;
        } catch (Exception e) {
            throw new E500Exception(e.getMessage());
        }

        return !queryResult.isEmpty();
    }

    public String getMerchantSecret(String name) throws E401Exception, E500Exception {
        List<Merchants> queryResult;
        try {
            Session session = hu.getSession();
            session.beginTransaction();

            queryResult = (List<Merchants>) session.createQuery("FROM Merchants WHERE name = :name").
                    setParameter("name", name).
                    list();

            session.getTransaction().commit();
            session.close();
        } catch (E500Exception e) {
            throw e;
        } catch (Exception e) {
            throw new E500Exception(e.getMessage());
        }

        if (queryResult.isEmpty() || queryResult.get(0).getSecret() == null || queryResult.get(0).getSecret().isEmpty()) {
            throw new E401Exception("Merchant secret is not set!");
        }

        return queryResult.get(0).getSecret();
    }

}