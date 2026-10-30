package com.example.todo.user.controller;

import com.example.todo.auth.dto.AuthDto.UserResponse;
import com.example.todo.user.entity.User;
import com.example.todo.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository users;

    public UserController(UserRepository users) {
        this.users = users;
    }

    @GetMapping("/me")
    public UserResponse me(Authentication authentication) {

        User user = users.findByEmail(authentication.getName())
                .orElseThrow();

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getNickname()
        );
    }
}
