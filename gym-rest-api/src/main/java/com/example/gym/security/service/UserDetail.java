package com.example.gym.security.service;

import com.example.gym.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Slf4j
public class UserDetail implements UserDetails {

    private Integer id;
    private String username;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetail build(User user) {
        //    List<String> strings = new ArrayList<>(List.of("ROLE_ADMIN", "ROLE_SUPPORTER", "ROLE_WORKER"));
        //    List<GrantedAuthority> authorities = strings.stream()
        //        .map(role -> new SimpleGrantedAuthority(role))
        //        .collect(Collectors.toList());
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
        UserDetail userDetails = new UserDetail(
                null,
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                authorities);
        log.debug("User Details: {}", userDetails);
        return userDetails;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}
