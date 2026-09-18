package com.backend.sanfely.order.controller;

import com.backend.sanfely.order.domain.OrderStatus;
import com.backend.sanfely.order.dto.OrderCreateRequestDto;
import com.backend.sanfely.order.dto.OrderResponseDto;
import com.backend.sanfely.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody OrderCreateRequestDto dto) {
        OrderResponseDto created = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('TRAITEUR', 'ADMIN')")
    public OrderResponseDto updateStatus(@PathVariable UUID id, @RequestParam OrderStatus status) {
        return orderService.updateStatus(id, status);
    }

    @GetMapping("/{id}")
    public OrderResponseDto getOrder(@PathVariable UUID id) {
        return orderService.getOrderById(id);
    }
}