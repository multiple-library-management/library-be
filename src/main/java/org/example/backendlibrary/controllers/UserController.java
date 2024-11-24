package org.example.backendlibrary.controllers;

import lombok.RequiredArgsConstructor;
import org.example.backendlibrary.entities.User;
import org.example.backendlibrary.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public void addUser(@RequestParam String name, @RequestParam int age) {
        userService.saveUser(name, age);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

}
