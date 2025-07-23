package com.strawhats.blogplatform.security.service;

import com.strawhats.blogplatform.model.User;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    private Long userId;
    private String username;
    private String email;
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> grantedAuthorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRoleName().name()))
                .collect(Collectors.toList());

        UserDetailsImpl userDetailsImpl = new UserDetailsImpl();
        userDetailsImpl.setUserId(user.getUserId());
        userDetailsImpl.setUsername(user.getUsername());
        userDetailsImpl.setEmail(user.getEmail());
        userDetailsImpl.setPassword(user.getPassword());
        userDetailsImpl.setAuthorities(grantedAuthorities);

        return userDetailsImpl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}

