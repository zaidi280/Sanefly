package com.backend.sanfely.payment.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;


import com.backend.sanfely.payment.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
	Optional<Payment> findByOrderId(UUID orderId);
}
