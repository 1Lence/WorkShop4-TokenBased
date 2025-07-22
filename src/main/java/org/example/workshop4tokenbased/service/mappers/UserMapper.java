package org.example.workshop4tokenbased.service.mappers;

import lombok.RequiredArgsConstructor;
import org.example.workshop4tokenbased.dto.UserDtoRequest;
import org.example.workshop4tokenbased.entity.User;
import org.example.workshop4tokenbased.entity.model.UserRoles;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoder passwordEncoder;

    //TODO: Добавить соль в пароль
    public User toUser(UserDtoRequest userDtoRequest) {
        return User.builder()
                .roles(UserRoles.valueOf(userDtoRequest.role()))
                .login(userDtoRequest.login())
                .email(userDtoRequest.email())
                .password(passwordEncoder.encode(userDtoRequest.password()))
                .build();
    }
}