package com.strawhats.blogplatform.service;

import com.strawhats.blogplatform.payload.UserDTO;
import com.strawhats.blogplatform.security.request.RegisterRequest;
import com.strawhats.blogplatform.security.response.JwtResponse;
import org.springframework.security.core.Authentication;

public interface AuthService {

    UserDTO signUp(RegisterRequest registerRequest);

    JwtResponse login(String username, String password);

    UserDTO deleteAccount(Authentication authentication);
}
