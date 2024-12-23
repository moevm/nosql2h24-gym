package com.example.gym.config;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service    
@RequiredArgsConstructor
public class AppService {

    private final Environment environment;

    public boolean isShow() {
        return environment.getActiveProfiles()[0].equals("show");
    }

    public boolean isProd() {
        return environment.getActiveProfiles()[0].equals("prod");
    }

}
