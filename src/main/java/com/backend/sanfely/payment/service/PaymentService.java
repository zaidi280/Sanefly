package com.backend.sanfely.payment.service;

import com.backend.sanfely.common.exception.ResourceNotFoundException;
import com.backend.sanfely.order.domain.Order;
import com.backend.sanfely.payment.domain.Payment;
import com.backend.sanfely.payment.domain.PaymentMethod;
import com.backend.sanfely.payment.dto.PaymentResponseDto;
import com.backend.sanfely.payment.mapper.PaymentMapper;
import com.backend.sanfely.payment.repository.PaymentRepository;
import com.backend.sanfely.payment.strategy.PaymentStrategy;
import com.backend.sanfely.payment.strategy.PaymentStrategyResolver;


import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentStrategyResolver strategyResolver;
    private final PaymentMapper paymentMapper;

    @Transactional
    public void initiatePayment(Order order, PaymentMethod method) {
        PaymentStrategy strategy = strategyResolver.resolve(method);
        strategy.process(order, order.getTotalPrice());

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(method);
        payment.setAmount(order.getTotalPrice());
        payment.setCompleted(false);

        paymentRepository.save(payment);
    }
    public PaymentResponseDto getPaymentByOrderId(UUID orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("payment not found with id: " + orderId));
        return paymentMapper.toResponseDto(payment);
    }
}