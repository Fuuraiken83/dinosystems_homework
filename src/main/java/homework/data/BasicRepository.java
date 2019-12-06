package homework.data;


public interface BasicRepository<T> {
    T add(T item);
    T update(T item);
    T delete(T item);
    T findById(long id);
    Iterable<T> findAll();
}
