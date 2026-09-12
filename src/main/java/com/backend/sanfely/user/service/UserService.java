package com.backend.sanfely.user.service;

import com.backend.sanfely.common.exception.ResourceNotFoundException;
import com.backend.sanfely.user.domain.User;
import com.backend.sanfely.user.dto.AdminUserResponseDto;
import com.backend.sanfely.user.dto.UserCreateRequestDto;
import com.backend.sanfely.user.dto.UserResponseDto;
import com.backend.sanfely.user.mapper.UserMapper;
import com.backend.sanfely.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDto createUser(UserCreateRequestDto dto) {
        User user = userMapper.toEntity(dto);
        User saved = userRepository.save(user);
        return userMapper.toResponseDto(saved);
    }

    public UserResponseDto getUserById(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return userMapper.toResponseDto(user);
    }
    @Transactional
    public void deactivateUser(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        user.setActive(false);
        userRepository.save(user);
    }

    public List<AdminUserResponseDto> getAllUsersForAdmin() {
        return userRepository.findAll()
            .stream()
            .map(userMapper::toAdminResponseDto)
            .toList();
    }
}