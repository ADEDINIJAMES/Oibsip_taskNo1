package com.example.atmdemoappforoasis.repository;

import com.example.atmdemoappforoasis.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
//    Optional<Users> findByEmail(String email);
    Optional<UserDetails> findByEmail(String email);
    Boolean existsByEmail(String email);
    List<Users> findByConfirmationExpirationBefore(LocalDateTime dateTime);


}
