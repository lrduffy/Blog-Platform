package com.strawhats.blogplatform.service;

import com.strawhats.blogplatform.payload.PagedResponse;
import com.strawhats.blogplatform.payload.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Override
    public UserDTO getUserProfile(Authentication authentication) {
        return null;
    }

    @Override
    public UserDTO updateUserProfile(Authentication authentication, UserDTO userDTO) {
        return null;
    }

    @Override
    public PagedResponse<UserDTO> searchUsers(String keyword, Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        return null;
    }

    @Override
    public PagedResponse<UserDTO> getUsers(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        return null;
    }

    @Override
    public PagedResponse<UserDTO> getUsersByUsername(String username, Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        return null;
    }

    @Override
    public PagedResponse<UserDTO> getUsersByEmail(String email, Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        return null;
    }
}
