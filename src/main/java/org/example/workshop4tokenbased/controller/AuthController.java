package org.example.workshop4tokenbased.controller;

import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.workshop4tokenbased.dto.JwtAuthDto;
import org.example.workshop4tokenbased.dto.RefreshTokenDto;
import org.example.workshop4tokenbased.dto.UserCredentialsDto;
import org.example.workshop4tokenbased.dto.UserDtoRequest;
import org.example.workshop4tokenbased.entity.User;
import org.example.workshop4tokenbased.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthDto> signIn(@RequestBody @Valid UserCredentialsDto userCredentialsDto) {
        try {
            JwtAuthDto dto = userService.signIn(userCredentialsDto);
            return ResponseEntity.ok(dto);
        }catch (AuthenticationException | JOSEException e) {
            throw new RuntimeException("Auth failed: " + e.getMessage());
        }
    }

    @PostMapping("/refresh")
    public JwtAuthDto refresh(@RequestBody RefreshTokenDto refreshTokenDto) throws AuthenticationException, JOSEException {
        return userService.refreshToken(refreshTokenDto);
    }

    @PostMapping("/registration")
    public User register(@RequestBody @Valid UserDtoRequest userDtoRequest) {
        return userService.saveUser(userDtoRequest);
    }
}