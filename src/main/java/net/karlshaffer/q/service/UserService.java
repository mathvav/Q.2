package net.karlshaffer.q.service;

import net.karlshaffer.q.model.Role;
import net.karlshaffer.q.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

public interface UserService extends UserDetailsService {
    Iterable<User> findAll();
    Iterable<User> findAllWithRole(Role role);
    User findByUsername(String username);
    User findById(Long id);
    User save(User user);
    void delete(User user);
    String login(String username, String password);
    String register(User user);
    User whoami(HttpServletRequest request);
}
