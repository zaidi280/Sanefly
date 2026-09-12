package com.backend.sanfely.order.domain;

public enum OrderStatus {
    PENDING,
    CONFIRMED,
    IN_PREPARATION,
    OUT_FOR_DELIVERY,
    DELIVERED,
    CANCELLED
}