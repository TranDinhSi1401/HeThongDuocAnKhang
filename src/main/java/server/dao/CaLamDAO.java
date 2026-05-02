package server.dao;

import server.entity.CaLam;

import java.util.List;

/**
 * DAO cho entity CaLam.
 */
public class CaLamDAO extends AbstractGenericDaoImpl<CaLam, String> {

    public CaLamDAO() {
        super(CaLam.class);
    }

    /** Tìm ca làm theo tên (LIKE). */
    public List<CaLam> timCaLamTheoTen(String tenCa) {
        return doInTransaction(em ->
            em.createQuery("SELECT cl FROM CaLam cl WHERE cl.tenCa LIKE :ten", CaLam.class)
              .setParameter("ten", "%" + tenCa + "%")
              .getResultList()
        );
    }
}
