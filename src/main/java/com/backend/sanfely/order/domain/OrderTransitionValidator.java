package com.backend.sanfely.order.domain;

import java.util.Map;
import java.util.Set;

public class OrderTransitionValidator {

    private OrderTransitionValidator() {
    }

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = Map.of(
        OrderStatus.PENDING,          Set.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED),
        OrderStatus.CONFIRMED,        Set.of(OrderStatus.IN_PREPARATION, OrderStatus.CANCELLED),
        OrderStatus.IN_PREPARATION,   Set.of(OrderStatus.OUT_FOR_DELIVERY, OrderStatus.CANCELLED),
        OrderStatus.OUT_FOR_DELIVERY, Set.of(OrderStatus.DELIVERED),
        OrderStatus.DELIVERED,        Set.of(),
        OrderStatus.CANCELLED,        Set.of()
    );

    public static boolean canTransition(OrderStatus from, OrderStatus to) {
        return ALLOWED_TRANSITIONS.getOrDefault(from, Set.of()).contains(to);
    }
}