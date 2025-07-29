package com.strawhats.blogplatform.controller;

import com.strawhats.blogplatform.payload.UserDTO;
import com.strawhats.blogplatform.security.request.LoginRequest;
import com.strawhats.blogplatform.security.request.RegisterRequest;
import com.strawhats.blogplatform.security.response.JwtResponse;
import com.strawhats.blogplatform.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDTO> signUp(@RequestBody RegisterRequest registerRequest) {
        UserDTO registeredUserDTO = authService.signUp(registerRequest);
        return new ResponseEntity<>(registeredUserDTO, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/account")
    public ResponseEntity<UserDTO> deleteAccount(Authentication authentication) {
        UserDTO deletedUserDTO = authService.deleteAccount(authentication);
        return new ResponseEntity<>(deletedUserDTO, HttpStatus.OK);
    }
}
