package org.example.workshop4tokenbased.entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum UserRoles implements GrantedAuthority {
    USER(Set.of(Permission.DEVELOPERS_READ)),
    USER_VIP(Set.of(Permission.DEVELOPERS_READ, Permission.DEVELOPERS_VIP_READ)),
    ADMIN(Set.of(Permission.DEVELOPERS_READ, Permission.DEVELOPERS_VIP_READ, Permission.DEVELOPERS_WRITE));

    private final Set<Permission> permissions;

    @Override
    public String getAuthority() {
        return this.name();
    }

    public Set<SimpleGrantedAuthority> getAuthorities() {
        return getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
    }
}