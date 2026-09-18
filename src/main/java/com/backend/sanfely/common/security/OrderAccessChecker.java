package com.backend.sanfely.common.security;

import com.backend.sanfely.order.domain.Order;
import com.backend.sanfely.user.domain.User;
import com.backend.sanfely.user.domain.UserRole;
import org.springframework.stereotype.Component;

@Component
public class OrderAccessChecker {

    public boolean canView(Order order, User currentUser) {
        boolean isClient = order.getClient().getId().equals(currentUser.getId());
        boolean isTraiteurOwner = order.getTraiteur().getUser().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == UserRole.ADMIN;
        return isClient || isTraiteurOwner || isAdmin;
    }
}