package org.example.workshop4tokenbased.service.mappers;

import org.example.workshop4tokenbased.dto.CustomUserDetails;
import org.example.workshop4tokenbased.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public CustomUserDetails toUserDetails(User user) {
        return CustomUserDetails
                .builder()
                .user(user)
                .authorities(user.getRoles().getAuthorities())
                .build();
    }
}