package org.example.workshop4tokenbased.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.workshop4tokenbased.security.jwt.JwtFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logout")
@RequiredArgsConstructor
public class LogOutController {
    private final JwtFilter jwtFilter;

    @PostMapping
    public void logOut(HttpServletRequest request) {
        jwtFilter.toBlackList(request);
    }
}