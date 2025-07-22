package org.example.workshop4tokenbased.security;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.workshop4tokenbased.dto.CustomUserDetails;
import org.example.workshop4tokenbased.entity.User;
import org.example.workshop4tokenbased.repository.UserRepository;
import org.example.workshop4tokenbased.service.mappers.CustomUserDetailsMapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceIml implements UserDetailsService {
    private final CustomUserDetailsMapper customUserDetailsMapper;
    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String login) throws EntityNotFoundException {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("User does not exists"));
        return customUserDetailsMapper.toUserDetails(user);
    }
}