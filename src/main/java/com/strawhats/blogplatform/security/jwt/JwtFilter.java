package com.strawhats.blogplatform.security.jwt;

import com.strawhats.blogplatform.model.User;
import com.strawhats.blogplatform.repository.UserRepository;
import com.strawhats.blogplatform.security.service.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("JwtFilter Started ...");
        try {

            String jwtToken = jwtUtils.getJwtTokenFromHeader(request);
            if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {

                String username = jwtUtils.getUsernameFromJwtToken(jwtToken);

                User user = userRepository.findUserByUsername(username).get();

                UserDetailsImpl userDetails = UserDetailsImpl.build(user);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Authentication Failed due to {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
