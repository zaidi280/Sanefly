package com.backend.sanfely.catalog.domain;

import com.backend.sanfely.common.audit.Auditable;
import com.backend.sanfely.traiteur.domain.Traiteur;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "dishes")
@Getter
@Setter
@NoArgsConstructor
public class Dish extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "traiteur_id", nullable = false)
    private Traiteur traiteur;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DishCategory category;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable = true;

    @Column(name = "prep_time_hours")
    private Integer prepTimeHours;

    @Version
    private Long version;
}