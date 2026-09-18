package com.backend.sanfely.common.security;

import com.backend.sanfely.user.domain.User;
import com.backend.sanfely.user.domain.UserRole;
import com.backend.sanfely.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CurrentUserProvider {

    private final UserRepository userRepository;

    @Transactional
    public User getCurrentUser() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = jwt.getClaimAsString("email");

        return userRepository.findByEmail(email)
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setFullName(jwt.getClaimAsString("name"));
                newUser.setPhone("");
                newUser.setRole(UserRole.CLIENT);
                return userRepository.save(newUser);
            });
    }
}