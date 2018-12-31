package net.karlshaffer.q.controller;

import net.karlshaffer.q.model.Role;
import net.karlshaffer.q.model.User;
import net.karlshaffer.q.model.dto.RegistrationDto;
import net.karlshaffer.q.service.RoleService;
import net.karlshaffer.q.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController extends ApiController {

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegistrationDto registrationInfo) {
        User newUser = new User();

        newUser.setUsername(registrationInfo.getUsername());
        newUser.setPassword(registrationInfo.getPassword());
        newUser.setRole(roleService.findByName("ROLE_ADMIN"));
        newUser.setEnabled(true);

        String token = userService.register(newUser);

        return token;
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        return userService.login(username, password);
    }

    @GetMapping("/users/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Iterable<User> getUsers() {
        return userService.findAll();
    }

    @PostMapping("/users/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User createUser(@RequestBody User user) {
        userService.register(user);
        return user;
    }

    @DeleteMapping("/users/")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User deleteUser(@RequestBody User user) {
        userService.delete(user);
        return user;
    }

}

