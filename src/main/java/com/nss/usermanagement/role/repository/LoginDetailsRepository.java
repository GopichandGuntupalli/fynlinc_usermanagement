package com.nss.usermanagement.role.repository;

import com.nss.usermanagement.role.entity.LoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoginDetailsRepository extends JpaRepository<LoginDetails,Long> {
    Optional<LoginDetails> findByEmail(String email);

    List<LoginDetails> findAllByEmail(String email);


    Optional<LoginDetails>  findByEmailAndToken(String email, String token);

}
