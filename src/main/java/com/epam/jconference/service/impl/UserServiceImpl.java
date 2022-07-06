package com.epam.jconference.service.impl;

import com.epam.jconference.model.User;
import com.epam.jconference.dto.UserDto;
import com.epam.jconference.mapper.UserMapper;
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

    @Override
    public UserDto create(UserDto user) {
        user.setRole(UserRole.USER);
        user.setNotifications(true);
        return UserMapper.INSTANCE.mapUserDto(userRepository.create(UserMapper.INSTANCE.mapUser(user)));
    }

    @Override
    public UserDto login(UserDto user) {
        return UserMapper.INSTANCE.mapUserDto(userRepository.login(UserMapper.INSTANCE.mapUser(user)));
    }

    @Override
    public UserDto update(UserDto userDto) {
        return UserMapper.INSTANCE.mapUserDto(userRepository.update(UserMapper.INSTANCE.mapUser(userDto)));
    }

    @Override
    public ResponseEntity<Void> logout() {
        return userRepository.logout();
    }

    @Override
    public UserDto profile() {
        return UserMapper.INSTANCE.mapUserDto(userRepository.profile());
    }

    @Override
    public UserDto getById(Long id) {
        return UserMapper.INSTANCE.mapUserDto(userRepository.getById(id));
    }
}
