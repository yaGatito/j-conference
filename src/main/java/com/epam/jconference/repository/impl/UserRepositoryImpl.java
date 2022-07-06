package com.epam.jconference.repository.impl;

import com.epam.jconference.bean.Session;
import com.epam.jconference.exception.EntityNotFoundException;
import com.epam.jconference.exception.UnauthorizedAccessException;
import com.epam.jconference.model.User;
import com.epam.jconference.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final List<User> users = new ArrayList<>();
    private final Session session;

    @Override
    public User create(User user) {
        if (session.getUser() == null) {
            users.add(user);
            int id = users.indexOf(user);
            user.setId((long) id);
            return user;
        } else {
            throw new UnauthorizedAccessException("You can have only one account");
        }
    }

    @Override
    public User update(User user) {
        User founded = users.get(user.getId().intValue());
        founded.setRole(user.getRole());
        founded.setName(user.getName());
        founded.setLastname(user.getLastname());
        founded.setNotifications(user.getNotifications());
        return null;
    }

    @Override
    public User login(User user) {
        User founded = users.stream().filter(u -> u.getEmail().equals(user.getEmail()) && u.getPassword().equals(user.getPassword())).findAny().orElseThrow(() -> new EntityNotFoundException("Mismatch of email and password"));
        session.setUser(founded);
        return founded;
    }

    @Override
    public ResponseEntity<Void> logout() {
        if (session.getUser() != null) {
            session.setUser(null);
            return ResponseEntity.ok().build();
        } else {
            throw new UnauthorizedAccessException("You aren't authorized");
        }
    }

    @Override
    public User profile() {
        if (session.getUser() != null) {
            return session.getUser();
        } else {
            throw new UnauthorizedAccessException("Authorize your account");
        }
    }

    @Override
    public User getById(Long id) {
        return users.get(id.intValue());
    }

}
