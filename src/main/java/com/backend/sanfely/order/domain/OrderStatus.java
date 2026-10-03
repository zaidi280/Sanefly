package com.backend.sanfely.order.domain;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    IN_PREPARATION,
    READY_FOR_PICKUP,
    ASSIGNED_TO_LIVREUR,
    PICKED_UP,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED
}