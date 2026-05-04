package server.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Utility class để quản lý EntityManagerFactory (singleton) cho JPA/Hibernate.
 * Tất cả DAO trong package server.dao dùng class này để lấy EntityManager.
 */
public class HibernateUtil {

    private static final String PERSISTENCE_UNIT = "mariadb-pu";
    private static EntityManagerFactory emf;

    private HibernateUtil() {}

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
        return emf;
    }

    public static EntityManager getEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
