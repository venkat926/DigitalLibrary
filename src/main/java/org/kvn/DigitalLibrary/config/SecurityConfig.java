package org.kvn.DigitalLibrary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Value("${student.authority}")
    private String studentAuthority;

    @Value("${admin.authority}")
    private String adminAuthority;

    // Authorization
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/user/addStudent/**").permitAll()
                        .requestMatchers("/user/addAdmin/**").permitAll()
                        .requestMatchers("/user/filter/**").hasAnyAuthority(studentAuthority, adminAuthority)
                        .requestMatchers("/book/addBook/**").hasAnyAuthority(adminAuthority)
                        .requestMatchers("/book/filter/**").hasAnyAuthority(studentAuthority, adminAuthority)
                        .requestMatchers("/txn/issue/**").hasAuthority(adminAuthority)
                        .requestMatchers("/txn/return/**").hasAuthority(adminAuthority)
                        .anyRequest().authenticated()
        ).formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());

        return http.build();
    }


    // PasswordEncoder
    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }
}