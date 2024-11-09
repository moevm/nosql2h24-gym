package com.example.gym.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.example.gym.exception.AuthenticationException;
import com.example.gym.exception.UniquenessViolationException;
import com.example.gym.model.dto.security.JwtResponse;
import com.example.gym.model.dto.security.LoginUserDto;
import com.example.gym.model.dto.security.RegisterUserDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.UserRoleType;
import com.example.gym.repository.UserRepository;
import com.example.gym.security.jwt.JwtTokenUtils;
import com.example.gym.security.service.UserDetailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UniquenessCheckService uniquenessCheckService;
    private final AuthenticationManager ayAuthenticationManager;
    private final UserDetailService userDetailService;
    private final JwtTokenUtils jwtTokenUtils;

    public void register(RegisterUserDto dto) throws UniquenessViolationException {
        if (uniquenessCheckService.findByEmail(dto.getEmail()).isPresent()) {
            throw new UniquenessViolationException("Пользователь с электронной почтой %s уже существует"
                    .formatted(dto.getEmail()));
        }

        if (uniquenessCheckService.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
            throw new UniquenessViolationException("Пользователь с номером телефона %s уже существует"
                    .formatted(dto.getPhoneNumber()));
        }

        User userToCreate = new User();
        userToCreate.setName(dto.getName());
        userToCreate.setSurname(dto.getSurname());
        userToCreate.setEmail(dto.getEmail());
        userToCreate.setPassword(dto.getPassword());
        userToCreate.setPhoneNumber(dto.getPhoneNumber());
        userToCreate.setRoleIndex(UserRoleType.ROLE_USER.ordinal());
        userToCreate.setRoles(List.of(UserRoleType.ROLE_USER.name()));
        userRepository.save(userToCreate);

    }

    public JwtResponse login(LoginUserDto dto) throws AuthenticationException {
        try {
            ayAuthenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        } catch (Exception exception) {
            throw new AuthenticationException("Неверные логин или пароль");
        }

        UserDetails userDetails = userDetailService.loadUserByUsername(dto.getEmail());
        String accessToken = jwtTokenUtils.generateAccessToken(userDetails, dto.getEmail());
        String refreshToken = jwtTokenUtils.generateRefreshToken(userDetails, dto.getEmail());
    
        return new JwtResponse(accessToken, refreshToken);
    }
    
}
