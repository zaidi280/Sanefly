package com.backend.sanfely.payment.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.backend.sanfely.payment.domain.PaymentMethod;

public record PaymentResponseDto( 
UUID id,
UUID orderId,
PaymentMethod method,
BigDecimal amount,
boolean completed
) {}
