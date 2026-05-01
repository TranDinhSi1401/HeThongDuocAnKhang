package server.dao;

import java.util.List;

/**
 * Generic DAO interface định nghĩa các thao tác CRUD cơ bản.
 *
 * @param <T>  Kiểu entity
 * @param <ID> Kiểu khóa chính
 */
public interface GenericDao<T, ID> {
    T create(T t);
    T update(T t);
    boolean delete(ID id);
    T findById(ID id);
    List<T> loadAll();
}
