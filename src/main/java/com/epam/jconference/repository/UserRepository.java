package com.epam.jconference.repository;

import com.epam.jconference.model.User;
import com.epam.jconference.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> getByEmail(String email);

    Boolean existsByEmail(String email);

    List<User> findAllByRole(UserRole role);
}
