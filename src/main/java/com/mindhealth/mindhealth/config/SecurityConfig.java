package com.mindhealth.mindhealth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import com.mindhealth.mindhealth.security.CustomOAuth2UserService;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/", "/home/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
                    "/static/**", "/css/**", "/js/**", "/images/**",
                    "/api/events/search", "/api/events/autocomplete"
                ).permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/events/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
                .userInfoEndpoint(cfg -> cfg.userService(customOAuth2UserService))
                .defaultSuccessUrl("/", true)
            )
            .formLogin(form -> form.loginPage("/login").permitAll())
            .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/"));
        return http.build();
    }
}
