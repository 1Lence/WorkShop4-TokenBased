package org.example.workshop4tokenbased.security;

import lombok.RequiredArgsConstructor;
import org.example.workshop4tokenbased.entity.User;
import org.example.workshop4tokenbased.repository.UserRepository;
import org.example.workshop4tokenbased.service.mappers.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceIml implements UserDetailsService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(login).orElseThrow(()->new UsernameNotFoundException("User does not exists"));
        return userMapper.toUserDetails(user);
    }
}