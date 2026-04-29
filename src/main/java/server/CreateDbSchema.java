package server;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class CreateDbSchema {
    public static void main(String[] args) {
        // viết code để tạo schema cho database ở đây
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("mariadb-pu");
            EntityManager em = emf.createEntityManager();

            em.getTransaction().begin();

            em.getTransaction().commit();
            emf.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Schema created successfully!");
    }
}
