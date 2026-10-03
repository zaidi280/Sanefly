package com.backend.sanfely.delivery.domain;

import com.backend.sanfely.common.audit.Auditable;
import com.backend.sanfely.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "delivery_companies")
@Getter
@Setter
@NoArgsConstructor
public class DeliveryCompany extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "verified_by_admin", nullable = false)
    private boolean verifiedByAdmin;

    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}