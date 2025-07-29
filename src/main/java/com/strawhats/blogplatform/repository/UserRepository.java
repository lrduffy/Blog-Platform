package com.strawhats.blogplatform.repository;

import com.strawhats.blogplatform.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor {

    Optional<User> findUserByUsername(String username);

    boolean existsUserByUsername(String username);

    Optional<User> findUserByUserId(Long userId);

    boolean existsUserByEmail(String email);

    Page<User> findAllByUsernameLike(String username,  Pageable pageable);

    Page<User> findAllByEmailLike(String email,   Pageable pageable);

    boolean existsUserByUserId(Long userId);
}
