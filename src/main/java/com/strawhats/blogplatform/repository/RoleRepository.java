package com.strawhats.blogplatform.repository;

import com.strawhats.blogplatform.model.AppRole;
import com.strawhats.blogplatform.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findRoleByRoleName(AppRole roleName);
}
