package com.backend.sanfely.order.event;

import com.backend.sanfely.order.domain.Order;

public record OrderCreatedEvent(Order order) {
}