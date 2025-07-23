package com.strawhats.blogplatform.security.config;

import com.strawhats.blogplatform.model.AppRole;
import com.strawhats.blogplatform.model.Role;
import com.strawhats.blogplatform.model.User;
import com.strawhats.blogplatform.repository.RoleRepository;
import com.strawhats.blogplatform.repository.UserRepository;
import com.strawhats.blogplatform.security.jwt.JwtEntryPoint;
import com.strawhats.blogplatform.security.jwt.JwtFilter;
import com.strawhats.blogplatform.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtEntryPoint jwtEntryPoint;
    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Role userRole = roleRepository.findRoleByRoleName(AppRole.ROLE_USER)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_USER)));

            Role adminRole = roleRepository.findRoleByRoleName(AppRole.ROLE_ADMIN)
                    .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));

            List<Role> adminRoles = Arrays.asList(userRole, adminRole);

            if (!userRepository.existsUserByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder().encode("adminPassword"));
                userRepository.save(admin);
            }

            userRepository.findUserByUsername("admin")
                    .ifPresent(user -> {
                        user.setRoles(adminRoles);
                        userRepository.save(user);
                    });
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        authenticationProvider.setUserDetailsService(userDetailsService);
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                    httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(jwtEntryPoint)
                ).cors(httpSecurityCorsConfigurer ->
                    httpSecurityCorsConfigurer.disable()
                ).sessionManagement(sessionManagement ->
                    sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                ).headers(
                        httpSecurityHeaders -> httpSecurityHeaders.frameOptions(frameOptionsConfig ->
                                    frameOptionsConfig.sameOrigin()
                                )
        ).authorizeHttpRequests(httpSecurityAuthorizeRequests ->
                        httpSecurityAuthorizeRequests.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/api/tests/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .requestMatchers("/h2-console/**").permitAll()
                                .anyRequest().authenticated()
                ).formLogin(httpSecurityFormLoginConfigurer ->
                    httpSecurityFormLoginConfigurer.disable()
                ).httpBasic(httpBasicConfigurer ->
                    httpBasicConfigurer.disable()
                ).addFilterBefore(
                        jwtFilter, UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring()
                .requestMatchers("/v2/api-docs",
                        "configuration/ui",
                        "swagger-resources/**",
                        "swagger-ui.html",
                        "configuration/security",
                        "webjars/**"));
    }
}
