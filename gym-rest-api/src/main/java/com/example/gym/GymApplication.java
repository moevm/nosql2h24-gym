package com.example.gym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GymApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(GymApplication.class);
        String branch = System.getenv("CI_COMMIT_BRANCH");
        String profile = "default";
        if ("prod".equals(branch) || "test".equals(branch)) {
            profile = "prod";
        } else if ("show".equals(branch)) {
            profile = "show";
        }
        app.setAdditionalProfiles(profile);
        app.run(args);

	}

}
	