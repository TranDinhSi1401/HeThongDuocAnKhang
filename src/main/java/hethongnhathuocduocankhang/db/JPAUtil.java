/*
 * @ (#) JPAUtil.java       1.0     5/2/2026
 *
 * Copyright (c) 2026 .
 * IUH.
 * All rights reserved.
 */
package hethongnhathuocduocankhang.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/*
 * @description:
 * @outhor: Khang
 * @date:   5/2/2026
 *version:  1.0
 */
public class JPAUtil {
    private static final String PERSISTENCE_UNIT_NAME = "mariadb-pu";
    private static EntityManagerFactory emf;
    private static EntityManager entityManager; //not thread safe

    static {
        emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}