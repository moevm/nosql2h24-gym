package com.example.gym.config;

import com.example.gym.model.user.UserRole;
import com.example.gym.model.user.UserRoleType;
import com.example.gym.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabasePreloader implements ApplicationRunner {

    private final UserRoleRepository userRoleRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        createRoles();
    }

    private void createRoles() {
        for (UserRoleType role : UserRoleType.values()) {
            if (userRoleRepository.findByName(role) == null) {
                userRoleRepository.save(new UserRole(role));
            }
        }
    }

}
