package com.nss.usermanagement.role.repository;

import com.nss.usermanagement.role.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail( String username, String email);

    @Query("SELECT u FROM User u WHERE " +
            "(:searchKey IS NULL OR " +
            "u.phoneNumber LIKE %:searchKey% OR " +
            "u.companyName LIKE %:searchKey% OR " +
            "u.description LIKE %:searchKey% OR " +
            "u.email LIKE %:searchKey% OR " +
            "u.username LIKE %:searchKey% OR " +
            "CONCAT(u.firstName, ' ', u.lastName) LIKE %:searchKey%)")
    Page<User> searchUsers(@Param("searchKey") String searchKey, Pageable pageable);


//
}
