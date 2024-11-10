package com.example.gym.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.gym.model.user.User;
import com.example.gym.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MyUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public MyUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with email %s not found".formatted(username)));
                
        return MyUserDetail.build(user);
    }

}
