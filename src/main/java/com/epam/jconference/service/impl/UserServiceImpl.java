package com.epam.jconference.service.impl;

import com.epam.jconference.bean.Session;
import com.epam.jconference.dto.UserDto;
import com.epam.jconference.exception.EntityNotFoundException;
import com.epam.jconference.exception.InvalidOperationException;
import com.epam.jconference.exception.UnauthorizedAccessException;
import com.epam.jconference.mapper.UserMapper;
import com.epam.jconference.model.User;
import com.epam.jconference.model.enums.UserRole;
import com.epam.jconference.repository.UserRepository;
import com.epam.jconference.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final Session session;

    private final UserMapper mapper = UserMapper.INSTANCE;

    @Override
    public UserDto create(UserDto user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new InvalidOperationException("User with specified email already exists");
        }
        user.setRole(UserRole.USER);
        user.setNotifications(true);
        return mapper.mapUserDto(userRepository.save(mapper.mapUser(user)));
    }

    @Override
    public UserDto login(UserDto user) {
        String email = user.getEmail();
        String password = user.getPassword();
        User persistedUser = userRepository.getByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " doesn't exist"));
        String persistedUserPassword = persistedUser.getPassword();
        if (!persistedUserPassword.equals(password)) {
            throw new UnauthorizedAccessException("Wrong password");
        }
        session.login(persistedUser);
        return mapper.mapUserDto(persistedUser);
    }

    @Override
    public UserDto update(UserDto userDto) {
        String email = userDto.getEmail();
        User persistedUser = userRepository.getByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " doesn't exist"));
        mapper.populateUserWithPresentUserDtoFields(persistedUser, userDto);
        persistedUser = userRepository.save(persistedUser);
        return mapper.mapUserDto(persistedUser);
    }

    @Override
    public ResponseEntity<Void> logout() {
        session.logout();
        return ResponseEntity.ok().build();
    }

    @Override
    public UserDto profile() {
        return mapper.mapUserDto(session.profile());
    }

    @Override
    public UserDto getById(Long id) {
        if (!userRepository.existsById(id)){
            throw new EntityNotFoundException("User doesn't exist");
        }
        return mapper.mapUserDto(userRepository.getById(id));
    }
}
