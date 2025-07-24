package org.example.workshop4tokenbased.service.mappers;

import org.example.workshop4tokenbased.dto.UserDtoResponse;
import org.example.workshop4tokenbased.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper implements BaseMapper<UserDtoResponse, User> {

    @Override
    public UserDtoResponse map(User user){
        return UserDtoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRoles().toString())
                .login(user.getLogin())
                .build();
    }
}