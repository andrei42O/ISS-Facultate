package persistance;

import model.User;

public interface IRepositoryDBUsers extends IRepository<Long, User> {
    User findByUsername(String username);
}
