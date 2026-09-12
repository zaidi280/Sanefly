package com.backend.sanfely.order.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderEventListener {

    @EventListener
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Order created: {}", event.order().getId());
        // later: trigger notification to traiteur
    }

    @EventListener
    public void onOrderStatusChanged(OrderStatusChangedEvent event) {
        log.info("Order {} changed from {} to {}",
            event.order().getId(), event.previousStatus(), event.newStatus());
        // later: cache eviction, notifications, review-prompt trigger, etc.
    }
}