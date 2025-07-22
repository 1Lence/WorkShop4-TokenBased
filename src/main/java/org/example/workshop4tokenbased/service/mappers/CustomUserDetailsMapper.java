package org.example.workshop4tokenbased.service.mappers;

import org.example.workshop4tokenbased.dto.CustomUserDetails;
import org.example.workshop4tokenbased.entity.User;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsMapper {

    public CustomUserDetails toUserDetails(User user) {
        return CustomUserDetails
                .builder()
                .user(user)
                .authorities(user.getRoles().getAuthorities())
                .build();
    }
}