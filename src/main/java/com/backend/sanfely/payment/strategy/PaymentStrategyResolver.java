package com.backend.sanfely.payment.strategy;

import com.backend.sanfely.payment.domain.PaymentMethod;
import com.backend.sanfely.payment.strategy.PaymentStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PaymentStrategyResolver {

    private final Map<PaymentMethod, PaymentStrategy> strategies;

    public PaymentStrategyResolver(List<PaymentStrategy> allStrategies) {
        this.strategies = allStrategies.stream()
            .collect(Collectors.toMap(PaymentStrategy::getSupportedMethod, Function.identity()));
    }

    public PaymentStrategy resolve(PaymentMethod method) {
        PaymentStrategy strategy = strategies.get(method);
        if (strategy == null) {
            throw new IllegalArgumentException("No payment strategy registered for method: " + method);
        }
        return strategy;
    }
}