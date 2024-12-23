package com.example.gym.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.gym.security.jwt.JwtAuthenticationEntryPoint;
import com.example.gym.security.jwt.JwtAuthenticationFilter;
import com.example.gym.security.service.MyUserDetailService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint entryPoint;
    private final MyUserDetailService userDetailService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(CsrfConfigurer::disable)
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(entryPoint))
                .sessionManagement(sessionManagement -> 
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(daoAuthenticationProvider())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/export").permitAll()
                        .requestMatchers("/import").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/register").permitAll()
                        .requestMatchers(
                                "/trainers", 
                                "/trainers/{trainerId}/trainings",
                                "/trainers/{trainerId}")
                                        .hasAnyRole("USER", "TRAINER", "ADMIN")

                        .requestMatchers(
                                "/trainings", 
                                "/trainings/{trainingId}", 
                                "/tarinigs/{trainingId}/registration/{clientId}")
                                        .hasAnyRole("USER", "TRAINER", "ADMIN")

                        .requestMatchers(HttpMethod.GET, "/rooms")
                                .hasAnyRole("ADMIN", "USER", "TRAINER")

                        .requestMatchers(HttpMethod.GET, "/rooms/{roomId}")
                                .hasAnyRole("ADMIN", "USER", "TRAINER")

                        .requestMatchers(HttpMethod.GET, "/clients", "/clients/{clientId}")
                                .hasAnyRole("USER", "TRAINER", "ADMIN")

                        .requestMatchers("/trainers/**").hasAnyRole("TRAINER", "ADMIN")
                        .requestMatchers("/clients/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/subscriptions/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/admins/**").hasRole("ADMIN")
                        .requestMatchers("/rooms/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(JwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .build(); 
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
          .requestMatchers("/swagger-ui/**", "/v3/api-docs*/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new NoOpPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        return daoAuthenticationProvider;
    }

    @Bean
    public JwtAuthenticationFilter JwtAuthenticationTokenFilter() {
      return new JwtAuthenticationFilter();
    }
  

}
