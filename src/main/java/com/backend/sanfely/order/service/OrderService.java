package com.backend.sanfely.order.service;

import com.backend.sanfely.catalog.domain.Dish;
import com.backend.sanfely.catalog.repository.DishRepository;
import com.backend.sanfely.common.exception.InvalidOrderTransitionException;
import com.backend.sanfely.common.exception.ResourceNotFoundException;
import com.backend.sanfely.order.domain.Order;
import com.backend.sanfely.order.domain.OrderItem;
import com.backend.sanfely.order.domain.OrderStatus;
import com.backend.sanfely.order.domain.OrderTransitionValidator;
import com.backend.sanfely.order.dto.OrderCreateRequestDto;
import com.backend.sanfely.order.dto.OrderItemRequestDto;
import com.backend.sanfely.order.dto.OrderResponseDto;
import com.backend.sanfely.order.event.OrderCreatedEvent;
import com.backend.sanfely.order.event.OrderStatusChangedEvent;
import com.backend.sanfely.order.mapper.OrderMapper;
import com.backend.sanfely.order.repository.OrderRepository;
import com.backend.sanfely.traiteur.domain.Traiteur;
import com.backend.sanfely.traiteur.repository.TraiteurRepository;
import com.backend.sanfely.user.domain.User;
import com.backend.sanfely.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final TraiteurRepository traiteurRepository;
    private final DishRepository dishRepository;
    private final OrderMapper orderMapper;
    private final OrderPricingService pricingService;
    private final ApplicationEventPublisher eventPublisher;
    @Transactional
    public OrderResponseDto createOrder(OrderCreateRequestDto dto) {
        User client = userRepository.findById(dto.clientId())
            .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + dto.clientId()));

        Traiteur traiteur = traiteurRepository.findById(dto.traiteurId())
            .orElseThrow(() -> new ResourceNotFoundException("Traiteur not found with id: " + dto.traiteurId()));

        Order order = new Order();
        order.setClient(client);
        order.setTraiteur(traiteur);
        order.setDeliveryAddress(dto.deliveryAddress());
        order.setRequestedDeliveryTime(dto.requestedDeliveryTime());

        for (OrderItemRequestDto itemDto : dto.items()) {
            Dish dish = dishRepository.findById(itemDto.dishId())
                .orElseThrow(() -> new ResourceNotFoundException("Dish not found with id: " + itemDto.dishId()));

            OrderItem item = new OrderItem();
            item.setDish(dish);
            item.setQuantity(itemDto.quantity());
            item.setUnitPriceSnapshot(dish.getPrice());

            order.addItem(item);
        }

        order.setTotalPrice(pricingService.calculateTotal(order.getItems()));

        Order saved = orderRepository.save(order);
        eventPublisher.publishEvent(new OrderCreatedEvent(saved)); 
        return orderMapper.toResponseDto(saved);
    }

    @Transactional
    public OrderResponseDto updateStatus(UUID orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        OrderStatus previousStatus = order.getStatus();
        if (!OrderTransitionValidator.canTransition(order.getStatus(), newStatus)) {
            throw new InvalidOrderTransitionException(
                "Cannot transition order from " + order.getStatus() + " to " + newStatus
            );
        }

        order.setStatus(newStatus);
        Order saved = orderRepository.save(order);
        eventPublisher.publishEvent(new OrderStatusChangedEvent(saved, previousStatus, newStatus));
        return orderMapper.toResponseDto(saved);
    }

    public OrderResponseDto getOrderById(UUID id) {
        Order order = orderRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return orderMapper.toResponseDto(order);
    }
}