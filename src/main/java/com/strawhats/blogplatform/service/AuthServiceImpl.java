package com.strawhats.blogplatform.service;

import com.strawhats.blogplatform.exception.UserAlreadyExistsException;
import com.strawhats.blogplatform.model.AppRole;
import com.strawhats.blogplatform.model.Role;
import com.strawhats.blogplatform.model.User;
import com.strawhats.blogplatform.payload.UserDTO;
import com.strawhats.blogplatform.repository.RoleRepository;
import com.strawhats.blogplatform.repository.UserRepository;
import com.strawhats.blogplatform.security.jwt.JwtUtils;
import com.strawhats.blogplatform.security.request.RegisterRequest;
import com.strawhats.blogplatform.security.response.JwtResponse;
import com.strawhats.blogplatform.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public UserDTO signUp(RegisterRequest registerRequest) {
        if (userRepository.existsUserByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username", registerRequest.getUsername());
        }

        if (userRepository.existsUserByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email", registerRequest.getEmail());
        }

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        List<Role> roles = new ArrayList<>();
        Role userRole = roleRepository.findRoleByRoleName(AppRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        roles.add(userRole);
        user.setRoles(roles);
        User savedUser = userRepository.save(user);

        UserDTO savedUserDTO = modelMapper.map(savedUser, UserDTO.class);
        return savedUserDTO;
    }

    @Override
    public JwtResponse login(String username, String password) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (AuthenticationException e) {
            log.error("Invalid username or password");
            throw new BadCredentialsException("Invalid username or password");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        JwtResponse jwtResponse = new JwtResponse();

        String jwtToken = jwtUtils.generateJwtTokenFromUsername(userDetails.getUsername());
        jwtResponse.setToken(jwtToken);

        jwtResponse.setUserId(userDetails.getUserId());
        jwtResponse.setUsername(userDetails.getUsername());
        jwtResponse.setEmail(userDetails.getEmail());

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.toList());

        jwtResponse.setRoles(roles);
        return jwtResponse;
    }

    @Override
    public UserDTO deleteAccount(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findUserByUserId(userDetails.getUserId())
                .orElseThrow();
        userRepository.delete(user);
        UserDTO deletedUserDTO = modelMapper.map(user, UserDTO.class);
        return deletedUserDTO;
    }
}
