package net.karlshaffer.q.service;

import net.karlshaffer.q.exceptions.WebApplicationException;
import net.karlshaffer.q.model.Role;
import net.karlshaffer.q.model.User;
import net.karlshaffer.q.repository.UserRepository;
import net.karlshaffer.q.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Iterable<User> findAllWithRole(Role role) {
        return userRepository.findAllByRole(role);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return user.get();
        }

        return null;
    }

    @Override
    public User save(User user) {
        User current = findByUsername(user.getUsername());

        if (current != null) {
            if (!current.getPassword().equals(user.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }

        return userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

    @Override
    public String login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            return jwtTokenProvider.createToken(username, userRepository.findByUsername(username).getRole());
        } catch (AuthenticationException e) {
            throw new WebApplicationException("Invalid username or password.", HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public String register(User user) {
        if (!userRepository.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            return jwtTokenProvider.createToken(user.getUsername(), user.getRole());
        } else {
            throw new WebApplicationException("Username already exists.", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public User whoami(HttpServletRequest request) {
        return userRepository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request)));
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = findByUsername(s);

        if (user != null) {
            return user;
        } else {
            throw new UsernameNotFoundException("Username not found");
        }
    }
}
