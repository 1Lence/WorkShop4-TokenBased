package org.example.workshop4tokenbased.service;

import com.nimbusds.jose.JOSEException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.workshop4tokenbased.dto.*;
import org.example.workshop4tokenbased.entity.User;
import org.example.workshop4tokenbased.entity.model.UserRoles;
import org.example.workshop4tokenbased.repository.UserRepository;
import org.example.workshop4tokenbased.security.jwt.JweService;
import org.example.workshop4tokenbased.service.mappers.UserDtoMapper;
import org.example.workshop4tokenbased.service.mappers.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserDtoMapper userDtoMapper;
    private final PasswordEncoder passwordEncoder;
    private final JweService jweService;

    public JwtAuthDto signIn(UserCredentialsDto userCredentialsDto) throws AuthenticationException, JOSEException {
        User user = findByUserCredentials(userCredentialsDto);
        return jweService.generateAuthToken(user.getLogin());
    }

    public JwtAuthDto refreshToken(RefreshTokenDto refreshTokenDto) throws AuthenticationException, JOSEException {
        String refreshedToken = refreshTokenDto.refreshToken();

        if(refreshedToken != null && jweService.validateJweToken(refreshedToken)) {
            User user = findByLogin(jweService.getLoginFromToken(refreshedToken));
            return jweService.refreshBaseToken(user.getLogin(), refreshedToken);
        }
        throw new AuthenticationException("Invalid refresh token");
    }

    @Transactional
    public User saveUser(UserDtoRequest user) {
        User entity = userMapper.map(user);

        if(userRepository.findByLogin(user.login()).isPresent()) {
            throw new EntityExistsException("User with login " + user.login() + " already exists");
        } else if (userRepository.findByEmail(user.email()).isPresent()) {
            throw new EntityExistsException("User with login " + user.login() + " already exists");
        }
        return userRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public UserDtoResponse findUserByLogin(String login) {
        return userDtoMapper.map(userRepository.findByLogin(login)
        .orElseThrow(() -> new EntityNotFoundException("User not found by login: " + login)));
    }

    @Transactional
    public UserDtoResponse setRole(UserDtoRequest userDtoRequest) {
        User user = findByLogin(userDtoRequest.login());

        user.setRoles(UserRoles.valueOf(userDtoRequest.role()));
        user.setPassword(passwordEncoder.encode(userDtoRequest.password()));
        user.setEmail(userDtoRequest.email());
        user.setLogin(userDtoRequest.login());

        return userDtoMapper.map(userRepository.save(user));
    }

    private User findByUserCredentials(UserCredentialsDto userCredentialsDto) throws AuthenticationException {
        Optional<User> optionalUser = userRepository.findByLogin(userCredentialsDto.login());

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (passwordEncoder.matches(userCredentialsDto.password(), user.getPassword())) {
                return user;
            }else{
                throw new AuthenticationException("Login or password incorrect");
            }
        }
        throw new EntityNotFoundException("User not found by login: " + userCredentialsDto.login());
    }

    private User findByLogin(String login) {
        return userRepository.findByLogin(login)
                .orElseThrow(() -> new EntityNotFoundException("There is not User with login: " + login));
    }
}