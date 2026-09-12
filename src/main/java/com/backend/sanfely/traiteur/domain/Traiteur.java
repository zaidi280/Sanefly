package com.backend.sanfely.traiteur.domain;

import com.backend.sanfely.common.audit.Auditable;
import com.backend.sanfely.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "traiteurs")
@Getter
@Setter
@NoArgsConstructor
public class Traiteur extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "verified_by_admin", nullable = false)
    private boolean verifiedByAdmin;

    @Column(name = "rating_avg")
    private Double ratingAvg;
 // Traiteur.java - add a field
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}