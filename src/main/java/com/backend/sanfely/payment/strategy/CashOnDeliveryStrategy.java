package com.backend.sanfely.payment.strategy;

import com.backend.sanfely.order.domain.Order;
import com.backend.sanfely.payment.domain.PaymentMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class CashOnDeliveryStrategy implements PaymentStrategy {

    @Override
    public PaymentMethod getSupportedMethod() {
        return PaymentMethod.CASH_ON_DELIVERY;
    }

    @Override
    public void process(Order order, BigDecimal amount) {
        log.info("Cash on delivery registered for order {} — amount {} due at delivery", order.getId(), amount);
        // no real payment processing needed here — money changes hands physically at delivery
    }
}