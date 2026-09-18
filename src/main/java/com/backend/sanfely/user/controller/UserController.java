package com.backend.sanfely.user.controller;


import com.backend.sanfely.user.dto.UserResponseDto;
import com.backend.sanfely.user.service.UserService;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        return ResponseEntity.ok(userService.syncCurrentUser());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}