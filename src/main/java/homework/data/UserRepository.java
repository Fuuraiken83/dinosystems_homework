package homework.data;

import homework.User;

public interface UserRepository {
    Iterable<User> findByName(String name);
}
