package com.strawhats.blogplatform.service;

import com.strawhats.blogplatform.model.User;
import com.strawhats.blogplatform.payload.PagedResponse;
import com.strawhats.blogplatform.payload.UserDTO;
import com.strawhats.blogplatform.repository.UserRepository;
import com.strawhats.blogplatform.repository.specification.UserSpecification;
import com.strawhats.blogplatform.security.service.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public UserDTO getUserProfile(Authentication authentication) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findUserByUserId(userDetails.getUserId()).get();

        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        return userDTO;
    }

    @Override
    public UserDTO updateUserProfile(Authentication authentication, UserDTO userDTO) {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findUserByUserId(userDetails.getUserId()).get();

        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());

        User updatedUser = userRepository.save(user);
        UserDTO updatedUserDTO = modelMapper.map(updatedUser, UserDTO.class);

        return updatedUserDTO;
    }

    @Override
    public PagedResponse<UserDTO> searchUsers(String keyword, Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Specification<User> userSpecification = UserSpecification.userContainsKeyword(keyword);
        Page<User> userPage = userRepository.findAll(userSpecification, pageable);

        List<UserDTO> content = userPage.getContent()
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();

        PagedResponse<UserDTO> userDTOPagedResponse = new PagedResponse<>();
        userDTOPagedResponse.setContent(content);
        userDTOPagedResponse.setPageNumber(userPage.getNumber());
        userDTOPagedResponse.setPageSize(userPage.getSize());
        userDTOPagedResponse.setTotalPages(userPage.getTotalPages());
        userDTOPagedResponse.setTotalElements(userPage.getTotalElements());
        userDTOPagedResponse.setIsLastPage(userPage.isLast());

        return userDTOPagedResponse;
    }

    @Override
    public PagedResponse<UserDTO> getUsers(Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        Page<User> userPage = userRepository.findAll(pageable);

        List<UserDTO> content = userPage
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();

        PagedResponse<UserDTO> userDTOPagedResponse = new PagedResponse<>();
        userDTOPagedResponse.setContent(content);
        userDTOPagedResponse.setPageNumber(userPage.getNumber());
        userDTOPagedResponse.setPageSize(userPage.getSize());
        userDTOPagedResponse.setTotalPages(userPage.getTotalPages());
        userDTOPagedResponse.setTotalElements(userPage.getTotalElements());
        userDTOPagedResponse.setIsLastPage(userPage.isLast());

        return userDTOPagedResponse;
    }

    @Override
    public PagedResponse<UserDTO> getUsersByUsername(String username, Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        String pattern = '%' + username + '%';
        Page<User> userPage = userRepository.findAllByUsernameLike(pattern);

        List<UserDTO> content = userPage
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();

        PagedResponse<UserDTO> userDTOPagedResponse = new PagedResponse<>();
        userDTOPagedResponse.setContent(content);
        userDTOPagedResponse.setPageNumber(userPage.getNumber());
        userDTOPagedResponse.setPageSize(userPage.getSize());
        userDTOPagedResponse.setTotalPages(userPage.getTotalPages());
        userDTOPagedResponse.setTotalElements(userPage.getTotalElements());
        userDTOPagedResponse.setIsLastPage(userPage.isLast());

        return userDTOPagedResponse;
    }

    @Override
    public PagedResponse<UserDTO> getUsersByEmail(String email, Integer pageNumber, Integer pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortField).ascending() : Sort.by(sortField);
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        String pattern = '%' + email + '%';
        Page<User> userPage = userRepository.findAllByEmailLike(email);

        List<UserDTO> content = userPage
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();

        PagedResponse<UserDTO> userDTOPagedResponse = new PagedResponse<>();
        userDTOPagedResponse.setContent(content);
        userDTOPagedResponse.setPageNumber(userPage.getNumber());
        userDTOPagedResponse.setPageSize(userPage.getSize());
        userDTOPagedResponse.setTotalPages(userPage.getTotalPages());
        userDTOPagedResponse.setTotalElements(userPage.getTotalElements());
        userDTOPagedResponse.setIsLastPage(userPage.isLast());

        return userDTOPagedResponse;
    }
}
