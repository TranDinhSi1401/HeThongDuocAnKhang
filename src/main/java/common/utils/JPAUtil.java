/*
 * @ (#) JPAUtil.java   1.0     04/05/2026
 *
 *Copyright (c) 2026 IUH. All rights reserved
 */


package common.utils;

/*
 * @description:
 * @author: Quan, Nguyen Khanh
 * @date: 04/05/2026
 * @version: 1.0
 * @created: 04/05/2026 11:11 CH
 */

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    public static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("mariadb-pu");
    public static EntityManager getEntityManger(){
        return emf.createEntityManager();
    }
}
