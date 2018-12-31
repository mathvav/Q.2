package net.karlshaffer.q.controller;

import net.karlshaffer.q.model.User;
import net.karlshaffer.q.model.dto.JwtAuthorizationDto;
import net.karlshaffer.q.model.dto.RegistrationDto;
import net.karlshaffer.q.service.RoleService;
import net.karlshaffer.q.service.UserService;
import net.karlshaffer.q.utility.ImageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

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

    @PutMapping(USERS_RESOURCE_PATH + "/{username}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> updateUser(HttpServletRequest request, @PathVariable String username, @RequestBody User user) {
        User currentUser = userService.whoami(request);
        User updateUser = userService.findByUsername(username);

        if (!currentUser.getRole().getName().equals("ROLE_ADMIN") && !currentUser.getUsername().equals(updateUser.getUsername())) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        if (user.getPassword() != null) {
            updateUser.setPassword(user.getPassword());
        }

        if (user.getRole() != null) {
            updateUser.setRole(user.getRole());
        }

        userService.save(updateUser);

        return new ResponseEntity<>(updateUser, HttpStatus.OK);
    }

    @PostMapping(USERS_RESOURCE_PATH + "/{username}/image")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> uploadProfileImage(HttpServletRequest request, @RequestParam("file") MultipartFile image, @PathVariable String username) {
        User user = userService.whoami(request);
        User desiredUser = userService.findByUsername(username);

        if (!user.getRole().getName().equals("ROLE_ADMIN") && !user.getUsername().equals(username)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        try {
            desiredUser.setImageType("image/jpeg");

            BufferedImage userImage = ImageIO.read(new ByteArrayInputStream(image.getBytes()));
            byte[] preparedUserImage = ImageUtility.prepareProfileImageForStorage(userImage);

            desiredUser.setImage(preparedUserImage);

            userService.save(desiredUser);
        } catch (IOException ioe) {
            //TODO: Handle this somehow.
        }

        return new ResponseEntity<>(desiredUser, HttpStatus.OK);
    }

    @GetMapping(value = USERS_RESOURCE_PATH + "/{username}/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getUserImage(@PathVariable String username) throws IOException {
        User user = userService.findByUsername(username);

        if (user.getImage() != null) {
            return user.getImage();
        } else {
            //TODO: Add a default image for users without an image.
            return null;
        }

    }


}

