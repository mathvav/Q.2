package net.karlshaffer.q.repository;

import net.karlshaffer.q.model.Role;
import net.karlshaffer.q.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
    Iterable<User> findAllByRole(Role role);
    boolean existsByUsername(String username);
}
