package com.strawhats.blogplatform.controller;

import com.strawhats.blogplatform.config.AppConstants;
import com.strawhats.blogplatform.payload.PagedResponse;
import com.strawhats.blogplatform.payload.UserDTO;
import com.strawhats.blogplatform.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/public/users/profile")
    public ResponseEntity<UserDTO> getUserProfile(Authentication authentication) {
        UserDTO userDTO = userService.getUserProfile(authentication);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/public/users/profile")
    public ResponseEntity<UserDTO> updateUserProfile(Authentication authentication, @RequestBody UserDTO userDTO) {
        UserDTO updatedUserDTO = userService.updateUserProfile(authentication, userDTO);
        return new ResponseEntity<>(updatedUserDTO, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users/search")
    public ResponseEntity<PagedResponse<UserDTO>> searchUsers(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE)  Integer pageSize,
            @RequestParam(value = "sortField", required = false, defaultValue = AppConstants.USER_SORT_FIELD) String sortField,
            @RequestParam(value = "sortDirection", required = false, defaultValue = AppConstants.SORT_DIRECTION) String sortDirection
    ) {
        PagedResponse<UserDTO> userDTOPagedResponse = userService.searchUsers(keyword, pageNumber, pageSize, sortField, sortDirection);
        return new ResponseEntity<>(userDTOPagedResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users")
    public ResponseEntity<PagedResponse<UserDTO>> getUsers(
            @RequestParam(value = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE)  Integer pageSize,
            @RequestParam(value = "sortField", required = false, defaultValue = AppConstants.USER_SORT_FIELD) String sortField,
            @RequestParam(value = "sortDirection", required = false, defaultValue = AppConstants.SORT_DIRECTION) String sortDirection
    ) {
        PagedResponse<UserDTO> userDTOPagedResponse = userService.getUsers(pageNumber, pageSize, sortField, sortDirection);
        return new ResponseEntity<>(userDTOPagedResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users/by-username")
    public ResponseEntity<PagedResponse<UserDTO>> getUsersByUsername(
            @RequestParam(value = "username",  required = false) String username,
            @RequestParam(value = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE)  Integer pageSize,
            @RequestParam(value = "sortField", required = false, defaultValue = AppConstants.USER_SORT_FIELD) String sortField,
            @RequestParam(value = "sortDirection", required = false, defaultValue = AppConstants.SORT_DIRECTION) String sortDirection
    ) {
        PagedResponse<UserDTO> userDTOPagedResponse = userService.getUsersByUsername(username, pageNumber, pageSize, sortField, sortDirection);
        return new ResponseEntity<>(userDTOPagedResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/users/by-email")
    public ResponseEntity<PagedResponse<UserDTO>> getUsersByEmail(
            @RequestParam(value = "email",  required = false) String email,
            @RequestParam(value = "pageNumber", required = false, defaultValue = AppConstants.PAGE_NUMBER) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = AppConstants.PAGE_SIZE)  Integer pageSize,
            @RequestParam(value = "sortField", required = false, defaultValue = AppConstants.USER_SORT_FIELD) String sortField,
            @RequestParam(value = "sortDirection", required = false, defaultValue = AppConstants.SORT_DIRECTION) String sortDirection
    ) {
        PagedResponse<UserDTO> userDTOPagedResponse = userService.getUsersByEmail(email, pageNumber, pageSize, sortField, sortDirection);
        return new ResponseEntity<>(userDTOPagedResponse, HttpStatus.OK);
    }

}
