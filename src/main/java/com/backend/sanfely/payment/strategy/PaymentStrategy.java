package com.backend.sanfely.payment.strategy;

import com.backend.sanfely.order.domain.Order;
import com.backend.sanfely.payment.domain.PaymentMethod;

import java.math.BigDecimal;

public interface PaymentStrategy {
    PaymentMethod getSupportedMethod();
    void process(Order order, BigDecimal amount);
}