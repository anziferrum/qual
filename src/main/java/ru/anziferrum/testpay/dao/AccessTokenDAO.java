package ru.anziferrum.testpay.dao;

import org.hibernate.Session;
import ru.anziferrum.testpay.entity.Tokens;
import ru.anziferrum.testpay.exception.E401Exception;
import ru.anziferrum.testpay.exception.E500Exception;
import ru.anziferrum.testpay.util.Util;

import java.util.*;

import static ru.anziferrum.testpay.util.Util.*;

/**
 * @author anziferrum
 */
public class AccessTokenDAO {
    private MerchantDAO merchantDAO = MerchantDAO.getInstance();

    private static AccessTokenDAO instance;

    private AccessTokenDAO() {}

    public static AccessTokenDAO getInstance() {
        if (instance == null) {
            instance = new AccessTokenDAO();
        }

        return instance;
    }

    public String getAccessToken(String[] credentials) throws E401Exception, E500Exception {
        String username = credentials[0];
        String password = credentials[1];

        if (!merchantDAO.checkMerchant(username, password)) {
            throw new E401Exception("User \"" + username + "\" does not exist either password is not valid!");
        }

        String token = getUserToken(username);

        if (token == null) {
            token = generateToken(username);
            storeToken(username, token);
        }

        return token;
    }

    public boolean checkToken(String token) throws E401Exception, E500Exception {
        boolean result;

        try {
            Session session = hu.getSession();
            session.beginTransaction();

            List<Tokens> queryResult = session.createQuery("FROM Tokens WHERE token = :token").
                setParameter("token", token).
                list();

            if (queryResult.isEmpty()) {
                result = false;    //no active user session
            } else {
                if (new Date().before(queryResult.get(0).getExpirationDate())) {
                    result = true;   //user session with actual token
                } else {
                    session = hu.getSession();
                    session.beginTransaction();

                    session.createQuery("DELETE FROM Tokens WHERE token = :token").
                            setParameter("token", token).
                            executeUpdate();

                    session.getTransaction().commit();
                    session.close();

                    result = false;
                }
            }

            session.getTransaction().commit();
            session.close();
        } catch (E500Exception e) {
            throw e;
        } catch (Exception e) {
            throw new E500Exception(e.getMessage());
        }

        return result;
    }

    public String getMerchantByToken(String token) throws E401Exception, E500Exception {
        try {
            Session session = hu.getSession();
            session.beginTransaction();

            List<Tokens> queryResult = (List<Tokens>) session.createQuery("FROM Tokens WHERE token = :token").
                    setParameter("token", token).
                    list();

            session.getTransaction().commit();
            session.close();

            if (queryResult.isEmpty()) {
                throw new E401Exception("No merchant session found!");
            }

            return queryResult.get(0).getUsername();
        } catch (E500Exception | E401Exception e) {
            throw e;
        } catch (Exception e) {
            throw new E500Exception(e.getMessage());
        }
    }

    private String getUserToken(String username) throws E401Exception, E500Exception {
        try {
            Session session = hu.getSession();
            session.beginTransaction();

            List<Tokens> queryResult = (List<Tokens>) session.createQuery("FROM Tokens WHERE username = :username").
                    setParameter("username", username).
                    list();

            session.getTransaction().commit();
            session.close();

            if (queryResult.isEmpty()) {
                return null;    //no active user session
            } else {
                if (new Date().before(queryResult.get(0).getExpirationDate())) {
                    return queryResult.get(0).getToken();   //user session with actual token
                } else {
                    session = hu.getSession();
                    session.beginTransaction();

                    session.createQuery("DELETE FROM Tokens WHERE username = :username").
                            setParameter("username", username).
                            executeUpdate();

                    session.getTransaction().commit();
                    session.close();

                    throw new E401Exception("Token for user \"" + username + "\" has expired!");
                }
            }
        } catch (E500Exception | E401Exception e) {
            throw e;
        } catch (Exception e) {
            throw new E500Exception(e.getMessage());
        }
    }

    private void storeToken(String username, String token) throws E401Exception, E500Exception {
        try {
            Session session = hu.getSession();
            session.beginTransaction();

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.SECOND, TOKEN_EXPIRATION_TIME);
            Tokens t = new Tokens(username, token, calendar.getTime());
            session.save(t);

            session.getTransaction().commit();
            session.close();
        } catch (E500Exception e) {
            throw e;
        } catch (Exception e) {
            throw new E500Exception(e.getMessage());
        }
    }


    private String generateToken(String user) {
        long base = System.currentTimeMillis();
        long salt = new Random(System.currentTimeMillis()).nextLong();

        return Util.md5(user) + Long.toHexString(base).toUpperCase() + Long.toHexString(salt).toUpperCase();
    }

}