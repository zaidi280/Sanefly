package com.backend.sanfely.order.domain;

import java.util.Map;
import java.util.Set;

public class OrderTransitionValidator {

    private OrderTransitionValidator() {
    }

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED_TRANSITIONS = Map.of(
    	    OrderStatus.PENDING,             Set.of(OrderStatus.CONFIRMED, OrderStatus.CANCELLED),
    	    OrderStatus.CONFIRMED,           Set.of(OrderStatus.IN_PREPARATION, OrderStatus.CANCELLED),
    	    OrderStatus.IN_PREPARATION,      Set.of(OrderStatus.READY_FOR_PICKUP, OrderStatus.CANCELLED),
    	    OrderStatus.READY_FOR_PICKUP,    Set.of(OrderStatus.ASSIGNED_TO_LIVREUR, OrderStatus.CANCELLED),
    	    OrderStatus.ASSIGNED_TO_LIVREUR, Set.of(OrderStatus.PICKED_UP),
    	    OrderStatus.PICKED_UP,           Set.of(OrderStatus.OUT_FOR_DELIVERY),
    	    OrderStatus.OUT_FOR_DELIVERY,    Set.of(OrderStatus.DELIVERED),
    	    OrderStatus.DELIVERED,           Set.of(),
    	    OrderStatus.CANCELLED,           Set.of()
    	);

    public static boolean canTransition(OrderStatus from, OrderStatus to) {
        return ALLOWED_TRANSITIONS.getOrDefault(from, Set.of()).contains(to);
    }
}