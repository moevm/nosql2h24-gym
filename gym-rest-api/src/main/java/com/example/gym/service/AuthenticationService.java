package com.example.gym.service;

import com.example.gym.model.dto.security.JwtResponse;
import com.example.gym.model.dto.security.LoginUserDto;
import com.example.gym.model.dto.security.RegisterUserDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.UserRoleType;
import com.example.gym.repository.UserRepository;
import com.example.gym.repository.UserRoleRepository;
import com.example.gym.security.jwt.JwtTokenUtils;
import com.example.gym.security.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UniquenessCheckService uniquenessCheckService;
    private final AuthenticationManager ayAuthenticationManager;
    private final UserDetailService userDetailService;
    private final JwtTokenUtils jwtTokenUtils;
    private final UserRoleRepository userRoleRepository;

    public void register(RegisterUserDto dto) {
        if (uniquenessCheckService.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с электронной почтой %s уже существует"
                    .formatted(dto.getEmail()));
        }

        if (uniquenessCheckService.findByPhoneNumber(dto.getPhoneNumber()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с номером телефона %s уже существует"
                    .formatted(dto.getPhoneNumber()));
        }

        User userToCreate = new User();
        userToCreate.setName(dto.getName());
        userToCreate.setSurname(dto.getSurname());
        userToCreate.setEmail(dto.getEmail());
        userToCreate.setPassword(dto.getPassword());
        userToCreate.setPhoneNumber(dto.getPhoneNumber());
        userToCreate.setRoleIndex(UserRoleType.ROLE_USER.ordinal());
        userToCreate.setRoles(Set.of(
                userRoleRepository.findByName(UserRoleType.ROLE_USER)));
        userRepository.save(userToCreate);

    }

    public JwtResponse login(LoginUserDto dto) {
        try {
            ayAuthenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        } catch (Exception exception) {
            throw new IllegalArgumentException("Неверные логин или пароль");
        }

        UserDetails userDetails = userDetailService.loadUserByUsername(dto.getEmail());
        String accessToken = jwtTokenUtils.generateAccessToken(userDetails, dto.getEmail());
        String refreshToken = jwtTokenUtils.generateRefreshToken(userDetails, dto.getEmail());
    
        return new JwtResponse(accessToken, refreshToken);
    }
    
}
