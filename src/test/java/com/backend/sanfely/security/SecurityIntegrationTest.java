package com.backend.sanfely.security;

import com.backend.sanfely.AbstractIntegrationTest;
import com.backend.sanfely.catalog.domain.Dish;
import com.backend.sanfely.catalog.domain.DishCategory;
import com.backend.sanfely.catalog.repository.DishRepository;
import com.backend.sanfely.order.domain.Order;
import com.backend.sanfely.order.domain.OrderStatus;
import com.backend.sanfely.order.repository.OrderRepository;
import com.backend.sanfely.traiteur.domain.Traiteur;
import com.backend.sanfely.traiteur.repository.TraiteurRepository;
import com.backend.sanfely.user.domain.User;
import com.backend.sanfely.user.domain.UserRole;
import com.backend.sanfely.user.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class SecurityIntegrationTest extends AbstractIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private UserRepository userRepository;
    @Autowired private TraiteurRepository traiteurRepository;
    @Autowired private DishRepository dishRepository;
    @Autowired private OrderRepository orderRepository;

    private User clientOwner;
    private User otherClient;
    private Order order;

    @BeforeEach
    void setUp() {
        clientOwner = new User();
        clientOwner.setEmail("owner@test.com");
        clientOwner.setPhone("20000000");
        clientOwner.setFullName("Owner Client");
        clientOwner.setRole(UserRole.CLIENT);
        clientOwner = userRepository.save(clientOwner);

        otherClient = new User();
        otherClient.setEmail("stranger@test.com");
        otherClient.setPhone("20000001");
        otherClient.setFullName("Stranger Client");
        otherClient.setRole(UserRole.CLIENT);
        otherClient = userRepository.save(otherClient);

        User traiteurOwner = new User();
        traiteurOwner.setEmail("traiteurowner@test.com");
        traiteurOwner.setPhone("20000002");
        traiteurOwner.setFullName("Traiteur Owner");
        traiteurOwner.setRole(UserRole.TRAITEUR);
        traiteurOwner = userRepository.save(traiteurOwner);

        Traiteur traiteur = new Traiteur();
        traiteur.setUser(traiteurOwner);
        traiteur.setBusinessName("Chez Test");
        traiteur.setVerifiedByAdmin(true);
        traiteur = traiteurRepository.save(traiteur);

        order = new Order();
        order.setClient(clientOwner);
        order.setTraiteur(traiteur);
        order.setDeliveryAddress("Test address");
        order.setStatus(OrderStatus.PENDING);
        order.setTotalPrice(new BigDecimal("25.00"));
        order = orderRepository.save(order);
    }
    @AfterEach
    void tearDown() {
        orderRepository.deleteAll();
        dishRepository.deleteAll();
        traiteurRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void adminEndpoint_forbiddenForClientRole() throws Exception {
        mockMvc.perform(get("/api/admin/traiteurs")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_CLIENT"))))
            .andExpect(status().isForbidden());
    }

    @Test
    void adminEndpoint_allowedForAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin/traiteurs")
                .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
            .andExpect(status().isOk());
    }

    @Test
    void orderView_forbiddenForNonOwner() throws Exception {
        mockMvc.perform(get("/api/orders/" + order.getId())
                .with(jwt()
                    .jwt(jwtToken -> jwtToken.claim("email", otherClient.getEmail()))
                    .authorities(new SimpleGrantedAuthority("ROLE_CLIENT"))))
            .andExpect(status().isForbidden());
    }

    @Test
    void orderView_allowedForActualOwner() throws Exception {
        mockMvc.perform(get("/api/orders/" + order.getId())
                .with(jwt()
                    .jwt(jwtToken -> jwtToken.claim("email", clientOwner.getEmail()))
                    .authorities(new SimpleGrantedAuthority("ROLE_CLIENT"))))
            .andExpect(status().isOk());
    }
}