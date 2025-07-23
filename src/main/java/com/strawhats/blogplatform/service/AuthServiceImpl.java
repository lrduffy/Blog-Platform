package com.strawhats.blogplatform.service;

import com.strawhats.blogplatform.payload.UserDTO;
import com.strawhats.blogplatform.security.request.RegisterRequest;
import com.strawhats.blogplatform.security.response.JwtResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public UserDTO signUp(RegisterRequest registerRequest) {
        return null;
    }

    @Override
    public JwtResponse login(String username, String password) {
        return null;
    }

    @Override
    public UserDTO deleteAccount(Authentication authentication) {
        return null;
    }
}
