package org.example.workshop4tokenbased.dto;

import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.example.workshop4tokenbased.entity.User;

import java.util.Collection;

@Builder
public record CustomUserDetails(User user,
                                Collection<? extends GrantedAuthority> authorities) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLogin();
    }
}
