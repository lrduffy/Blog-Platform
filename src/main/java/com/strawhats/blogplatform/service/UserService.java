package com.strawhats.blogplatform.service;

import com.strawhats.blogplatform.payload.PagedResponse;
import com.strawhats.blogplatform.payload.UserDTO;
import org.springframework.security.core.Authentication;

public interface UserService {

    UserDTO getUserProfile(Authentication authentication);

    UserDTO updateUserProfile(Authentication authentication, UserDTO userDTO);

    PagedResponse<UserDTO> searchUsers(String keyword, Integer pageNumber, Integer pageSize, String sortField, String sortDirection);

    PagedResponse<UserDTO> getUsers(Integer pageNumber, Integer pageSize, String sortField, String sortDirection);

    PagedResponse<UserDTO> getUsersByUsername(String username, Integer pageNumber, Integer pageSize, String sortField, String sortDirection);

    PagedResponse<UserDTO> getUsersByEmail(String email, Integer pageNumber, Integer pageSize, String sortField, String sortDirection);
}
