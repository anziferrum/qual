package ru.anziferrum.testpay.util;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ru.anziferrum.testpay.entity.Tokens;
import ru.anziferrum.testpay.entity.Transactions;
import ru.anziferrum.testpay.entity.Merchants;
import ru.anziferrum.testpay.exception.E500Exception;

/**
 * @author anziferrum
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;

    private static HibernateUtil instance;

    public static HibernateUtil getInstance() {
        if (instance == null) {
            instance = new HibernateUtil();
        }

        return instance;
    }

    private HibernateUtil() {}

    private SessionFactory getSessionFactory() throws E500Exception {
        if (sessionFactory == null) {
            Configuration configuration = new Configuration().configure(HibernateUtil.class.getResource("/hibernate.cfg.xml"));

            configuration.addAnnotatedClass(Merchants.class);
            configuration.addAnnotatedClass(Tokens.class);
            configuration.addAnnotatedClass(Transactions.class);

            StandardServiceRegistryBuilder serviceRegistryBuilder = new StandardServiceRegistryBuilder();
            serviceRegistryBuilder.applySettings(configuration.getProperties());
            ServiceRegistry serviceRegistry = serviceRegistryBuilder.build();
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }

        //filling the DB, just for example
        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            if (session.createQuery("FROM Merchants").list().isEmpty()) {
                Merchants first = new Merchants("asdf", Util.md5("ghjk"), "123");
                session.save(first);
                Merchants second = new Merchants("admin", Util.md5("admin"), "123");
                session.save(second);
                Merchants third = new Merchants("vasya", Util.md5("pupkin"), "123");
                session.save(third);
            }

            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            throw new E500Exception(e.getMessage());
        }

        return sessionFactory;
    }

    public Session getSession() throws E500Exception {
        return getSessionFactory().isOpen() ? sessionFactory.getCurrentSession() : sessionFactory.openSession();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();

        try {
            sessionFactory.close();
        } catch (HibernateException e) {
            sessionFactory = null;
        }
    }
}