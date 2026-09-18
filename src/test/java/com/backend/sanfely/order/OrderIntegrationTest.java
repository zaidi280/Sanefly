package com.backend.sanfely.order;

import com.backend.sanfely.AbstractIntegrationTest;
import com.backend.sanfely.catalog.domain.Dish;
import com.backend.sanfely.catalog.domain.DishCategory;
import com.backend.sanfely.catalog.repository.DishRepository;
import com.backend.sanfely.order.domain.OrderStatus;
import com.backend.sanfely.order.dto.OrderCreateRequestDto;
import com.backend.sanfely.order.dto.OrderItemRequestDto;
import com.backend.sanfely.order.dto.OrderResponseDto;
import com.backend.sanfely.order.service.OrderService;
import com.backend.sanfely.traiteur.domain.Traiteur;
import com.backend.sanfely.traiteur.repository.TraiteurRepository;
import com.backend.sanfely.user.domain.User;
import com.backend.sanfely.user.domain.UserRole;
import com.backend.sanfely.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderIntegrationTest extends AbstractIntegrationTest {

    @Autowired private OrderService orderService;
    @Autowired private UserRepository userRepository;
    @Autowired private TraiteurRepository traiteurRepository;
    @Autowired private DishRepository dishRepository;

    @Test
    void createOrder_persistsCorrectlyWithRealDatabase() {
        User client = new User();
        client.setEmail("ahmed@test.com");
        client.setPhone("20123456");
        client.setFullName("Ahmed Ben Salah");
        client.setRole(UserRole.CLIENT);
        client = userRepository.save(client);

        User traiteurOwner = new User();
        traiteurOwner.setEmail("amel@test.com");
        traiteurOwner.setPhone("20999999");
        traiteurOwner.setFullName("Amel Ben Ali");
        traiteurOwner.setRole(UserRole.TRAITEUR);
        traiteurOwner = userRepository.save(traiteurOwner);

        Traiteur traiteur = new Traiteur();
        traiteur.setUser(traiteurOwner);
        traiteur.setBusinessName("Chez Amel");
        traiteur.setVerifiedByAdmin(true);
        traiteur = traiteurRepository.save(traiteur);

        Dish dish = new Dish();
        dish.setTraiteur(traiteur);
        dish.setName("Couscous Royal");
        dish.setPrice(new BigDecimal("25.00"));
        dish.setCategory(DishCategory.COUSCOUS);
        dish.setAvailable(true);
        dish = dishRepository.save(dish);

        OrderItemRequestDto itemDto = new OrderItemRequestDto(dish.getId(), 2);
        OrderCreateRequestDto dto = new OrderCreateRequestDto(
            client.getId(), "Ariana, Tunisia", null, List.of(itemDto)
        );

        OrderResponseDto result = orderService.createOrder(dto);

        assertThat(result.status()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.totalPrice()).isEqualByComparingTo("50.00");
        assertThat(result.items()).hasSize(1);
    }
}