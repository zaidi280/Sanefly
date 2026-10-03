package com.backend.sanfely.delivery.domain;

import com.backend.sanfely.order.domain.Order;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "delivery_offers")
@Getter
@Setter
@NoArgsConstructor
public class DeliveryOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "livreur_id", nullable = false)
    private Livreur livreur;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryOfferStatus status;

    @Column(name = "offered_at", nullable = false)
    private LocalDateTime offeredAt;

    @Column(name = "responded_at")
    private LocalDateTime respondedAt;

   
}