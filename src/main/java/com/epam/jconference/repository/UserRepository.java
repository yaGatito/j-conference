package com.epam.jconference.repository;

import com.epam.jconference.model.User;
import org.springframework.http.ResponseEntity;

public interface UserRepository {
    User create(User user);

    User update(User user);

    User login(User user);

    ResponseEntity<Void> logout();

    User profile();

    User getById(Long id);
}
