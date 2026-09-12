package com.backend.sanfely.order.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTransitionValidatorTest {

    @ParameterizedTest
    @CsvSource({
        "PENDING, CONFIRMED, true",
        "PENDING, CANCELLED, true",
        "PENDING, DELIVERED, false",
        "PENDING, IN_PREPARATION, false",
        "CONFIRMED, IN_PREPARATION, true",
        "CONFIRMED, CANCELLED, true",
        "CONFIRMED, DELIVERED, false",
        "IN_PREPARATION, OUT_FOR_DELIVERY, true",
        "IN_PREPARATION, CANCELLED, true",
        "IN_PREPARATION, PENDING, false",
        "OUT_FOR_DELIVERY, DELIVERED, true",
        "OUT_FOR_DELIVERY, CANCELLED, false",
        "DELIVERED, CANCELLED, false",
        "DELIVERED, PENDING, false",
        "CANCELLED, PENDING, false",
        "CANCELLED, CONFIRMED, false"
    })
    void canTransition_returnsExpectedResult(OrderStatus from, OrderStatus to, boolean expected) {
        boolean result = OrderTransitionValidator.canTransition(from, to);
        assertThat(result).isEqualTo(expected);
    }
}