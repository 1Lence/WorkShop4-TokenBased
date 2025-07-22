package org.example.workshop4tokenbased.controller;

import lombok.RequiredArgsConstructor;
import org.example.workshop4tokenbased.dto.UserDtoResponse;
import org.example.workshop4tokenbased.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/{login}")
    public UserDtoResponse getUserByLogin(@PathVariable String login) {
        return userService.findUserByLogin(login);
    }
}