package org.example.workshop4tokenbased.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.workshop4tokenbased.dto.UserDtoRequest;
import org.example.workshop4tokenbased.dto.UserDtoResponse;
import org.example.workshop4tokenbased.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;

    @GetMapping
    public String admin() {
        return "This is Admin Controller";
    }

    @PutMapping
    public UserDtoResponse changeUserData(@RequestBody @Valid UserDtoRequest userDtoRequest) {
        return userService.setRole(userDtoRequest);
    }
}