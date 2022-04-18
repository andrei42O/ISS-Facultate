package persistance;

import java.util.List;

public interface IRepository<ID, E> {
    E save(E entity);
    List<E> getAll();
    E delete(E entity);
    E update(E entity, ID id);
}
