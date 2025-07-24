package org.example.workshop4tokenbased.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.workshop4tokenbased.security.jwt.JweFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logout")
@RequiredArgsConstructor
public class LogOutController {
    private final JweFilter jweFilter;

    @PostMapping
    public void logOut(HttpServletRequest request) {
        jweFilter.toBlackList(request);
    }
}