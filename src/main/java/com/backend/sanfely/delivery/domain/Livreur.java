package com.backend.sanfely.delivery.domain;

import com.backend.sanfely.common.audit.Auditable;
import com.backend.sanfely.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "livreurs")
@Getter
@Setter
@NoArgsConstructor
public class Livreur extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "delivery_company_id", nullable = false)
    private DeliveryCompany deliveryCompany;

    @Column(name = "is_available", nullable = false)
    private boolean available = true;

    @Column(name = "current_latitude")
    private BigDecimal currentLatitude;

    @Column(name = "current_longitude")
    private BigDecimal currentLongitude;

    @Column(name = "last_location_update")
    private LocalDateTime lastLocationUpdate;
 // Livreur.java - add this field
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}