package com.example.gym.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.example.gym.exception.AuthenticationException;
import com.example.gym.exception.UniquenessViolationException;
import com.example.gym.model.dto.security.JwtResponse;
import com.example.gym.model.dto.security.LoginUserDto;
import com.example.gym.model.dto.security.RegisterUserDto;
import com.example.gym.model.user.User;
import com.example.gym.model.user.UserRoleType;
import com.example.gym.model.user.pojo.ClientInfo;
import com.example.gym.repository.UserRepository;
import com.example.gym.security.jwt.JwtTokenUtils;
import com.example.gym.security.service.MyUserDetail;
import com.example.gym.security.service.MyUserDetailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final UniquenessCheckService uniquenessCheckService;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailService userDetailService;
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
        userToCreate.setRoles(List.of(UserRoleType.ROLE_USER.name()));

        ClientInfo clientInfo = new ClientInfo(0, new ArrayList<>(), new ArrayList<>());
        userToCreate.setClientInfo(clientInfo);
        userRepository.save(userToCreate);

    }

    public JwtResponse login(LoginUserDto dto) throws AuthenticationException {
        System.out.println(userRepository.findByEmail(dto.getEmail()).get());
        System.out.println(dto.getEmail());
        System.out.println(dto.getPassword());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        } catch (Exception exception) {
            throw new AuthenticationException("Неверные логин или пароль");
        }

        MyUserDetail userDetails = userDetailService.loadUserByUsername(dto.getEmail());
        String accessToken = jwtTokenUtils.generateAccessToken(userDetails, dto.getEmail());
        String refreshToken = jwtTokenUtils.generateRefreshToken(userDetails, dto.getEmail());
    
        return new JwtResponse(accessToken, refreshToken);
    }
    
}
