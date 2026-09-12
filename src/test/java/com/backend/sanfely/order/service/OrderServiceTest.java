package com.backend.sanfely.order.service;

import com.backend.sanfely.catalog.domain.Dish;
import com.backend.sanfely.catalog.repository.DishRepository;
import com.backend.sanfely.common.exception.ResourceNotFoundException;
import com.backend.sanfely.order.domain.Order;
import com.backend.sanfely.order.dto.OrderCreateRequestDto;
import com.backend.sanfely.order.dto.OrderItemRequestDto;
import com.backend.sanfely.order.dto.OrderResponseDto;
import com.backend.sanfely.order.mapper.OrderMapper;
import com.backend.sanfely.order.repository.OrderRepository;
import com.backend.sanfely.traiteur.domain.Traiteur;
import com.backend.sanfely.traiteur.repository.TraiteurRepository;
import com.backend.sanfely.user.domain.User;
import com.backend.sanfely.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock private TraiteurRepository traiteurRepository;
    @Mock private DishRepository dishRepository;
    @Mock private OrderMapper orderMapper;
    @Mock private ApplicationEventPublisher eventPublisher;

    private OrderPricingService pricingService;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        pricingService = new OrderPricingService(); // real, no dependencies needed
        orderService = new OrderService(
            orderRepository, userRepository, traiteurRepository,
            dishRepository, orderMapper, pricingService, eventPublisher
        );
    }

    @Test
    void createOrder_throwsWhenClientNotFound() {
        UUID fakeClientId = UUID.randomUUID();
        OrderCreateRequestDto dto = new OrderCreateRequestDto(
            fakeClientId, UUID.randomUUID(), "Some address", null, List.of()
        );

        when(userRepository.findById(fakeClientId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(dto))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Client not found");
    }

    @Test
    void createOrder_throwsWhenDishNotFound() {
        UUID clientId = UUID.randomUUID();
        UUID traiteurId = UUID.randomUUID();
        UUID missingDishId = UUID.randomUUID();

        User client = new User();
        Traiteur traiteur = new Traiteur();

        OrderItemRequestDto itemDto = new OrderItemRequestDto(missingDishId, 2);
        OrderCreateRequestDto dto = new OrderCreateRequestDto(
            clientId, traiteurId, "Some address", null, List.of(itemDto)
        );

        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(traiteurRepository.findById(traiteurId)).thenReturn(Optional.of(traiteur));
        when(dishRepository.findById(missingDishId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(dto))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("Dish not found");
    }

    @Test
    void createOrder_calculatesCorrectTotalAndSaves() {
        UUID clientId = UUID.randomUUID();
        UUID traiteurId = UUID.randomUUID();
        UUID dishId = UUID.randomUUID();

        User client = new User();
        Traiteur traiteur = new Traiteur();

        Dish dish = new Dish();
        dish.setPrice(new BigDecimal("15.00"));

        OrderItemRequestDto itemDto = new OrderItemRequestDto(dishId, 3);
        OrderCreateRequestDto dto = new OrderCreateRequestDto(
            clientId, traiteurId, "Some address", null, List.of(itemDto)
        );

        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(traiteurRepository.findById(traiteurId)).thenReturn(Optional.of(traiteur));
        when(dishRepository.findById(dishId)).thenReturn(Optional.of(dish));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(orderMapper.toResponseDto(any(Order.class))).thenReturn(mock(OrderResponseDto.class));

        orderService.createOrder(dto);

        verify(orderRepository).save(argThat(order ->
            order.getTotalPrice().compareTo(new BigDecimal("45.00")) == 0
        ));
        verify(eventPublisher).publishEvent(any(Object.class));
    }
}