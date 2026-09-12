package com.backend.sanfely.user.domain;

import java.util.UUID;

import com.backend.sanfely.common.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(columnDefinition = "uuid", updatable = false, nullable = false)
	private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(name = "full_name")
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
 // User.java - add a field
    @Column(name = "is_active", nullable = false)
    private boolean active = true;
}