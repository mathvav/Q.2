package net.karlshaffer.q.controller;

import net.karlshaffer.q.model.User;
import net.karlshaffer.q.model.dto.JwtAuthorizationDto;
import net.karlshaffer.q.model.dto.RegistrationDto;
import net.karlshaffer.q.service.RoleService;
import net.karlshaffer.q.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController extends ApiController {

    public static final String USERS_RESOURCE_PATH = "/users";

    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthorizationDto> register(@RequestBody RegistrationDto registrationInfo) {
        User newUser = new User();

        newUser.setUsername(registrationInfo.getUsername());
        newUser.setPassword(registrationInfo.getPassword());
        newUser.setRole(roleService.findByName("ROLE_ADMIN"));
        newUser.setEnabled(true);

        String token = userService.register(newUser);

        JwtAuthorizationDto authorizationDto = JwtAuthorizationDto.builder().token(token).build();

        return new ResponseEntity<>(authorizationDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthorizationDto> login(@RequestParam String username, @RequestParam String password) {
        String token = userService.login(username, password);

        JwtAuthorizationDto authorizationDto = JwtAuthorizationDto.builder().token(token).build();

        return new ResponseEntity<>(authorizationDto, HttpStatus.OK);
    }

    @GetMapping(USERS_RESOURCE_PATH)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Iterable<User> getUsers() {
        return userService.findAll();
    }

    @PostMapping(USERS_RESOURCE_PATH)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User createUser(@RequestBody User user) {
        userService.register(user);
        return user;
    }

    @DeleteMapping(USERS_RESOURCE_PATH + "/{userId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> deleteUser(@PathVariable Long userId) {
        User user = userService.findById(userId);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userService.delete(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}

