package ru.itmo.wp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.itmo.wp.domain.User;
import ru.itmo.wp.service.JwtService;
import ru.itmo.wp.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/1")
public class UserController {
    private final JwtService jwtService;
    private final UserService userService;

    public UserController(JwtService jwtService, UserService userService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @GetMapping("users")
    public List<User> Users() {
        return userService.findAll();
    }

    @GetMapping("users/auth")
    public User findUserByJwt(@RequestParam String jwt) {
        return jwtService.find(jwt);
    }
}
