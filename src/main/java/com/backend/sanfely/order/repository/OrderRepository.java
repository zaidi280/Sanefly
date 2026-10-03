package com.backend.sanfely.order.repository;

import com.backend.sanfely.order.domain.Order;
import com.backend.sanfely.order.domain.OrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByClientId(UUID clientId);
    List<Order> findByTraiteurId(UUID traiteurId);
 // OrderRepository.java - add this method
    List<Order> findByStatus(OrderStatus status);
}