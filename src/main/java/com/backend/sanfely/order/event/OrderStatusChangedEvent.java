package com.backend.sanfely.order.event;

import com.backend.sanfely.order.domain.Order;
import com.backend.sanfely.order.domain.OrderStatus;

public record OrderStatusChangedEvent(Order order, OrderStatus previousStatus, OrderStatus newStatus) {
}