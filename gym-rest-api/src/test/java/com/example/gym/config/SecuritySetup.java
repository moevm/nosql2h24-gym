package com.example.gym.config;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.example.gym.security.jwt.JwtTokenUtils;
import com.example.gym.security.service.MyUserDetail;

@Component
@Profile("test")
public class SecuritySetup {

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private UserDetailsService userDetailsService;

    public void setUpSecurityContext(String username, List<String> roles) {
        List<SimpleGrantedAuthority> authorities = roles.stream()
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username, 
                "password", 
                authorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    public String createTestToken(String username) {
        MyUserDetail userDetails = (MyUserDetail) userDetailsService.loadUserByUsername(username);
        
        return jwtTokenUtils.generateAccessToken(userDetails, userDetails.getEmail());
    }

}
