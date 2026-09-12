package com.backend.sanfely.order.service;

import com.backend.sanfely.catalog.domain.Dish;
import com.backend.sanfely.order.domain.OrderItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderPricingServiceTest {

    private final OrderPricingService pricingService = new OrderPricingService();

    @Test
    void calculateTotal_sumsAllItemSubtotalsCorrectly() {
        OrderItem item1 = new OrderItem();
        item1.setQuantity(2);
        item1.setUnitPriceSnapshot(new BigDecimal("15.00"));

        OrderItem item2 = new OrderItem();
        item2.setQuantity(1);
        item2.setUnitPriceSnapshot(new BigDecimal("10.00"));

        BigDecimal total = pricingService.calculateTotal(List.of(item1, item2));

        assertThat(total).isEqualByComparingTo("40.00");
    }

    @Test
    void calculateTotal_returnsZeroForEmptyList() {
        BigDecimal total = pricingService.calculateTotal(List.of());

        assertThat(total).isEqualByComparingTo("0");
    }

    @Test
    void calculateTotal_handlesSingleItem() {
        OrderItem item = new OrderItem();
        item.setQuantity(3);
        item.setUnitPriceSnapshot(new BigDecimal("5.50"));

        BigDecimal total = pricingService.calculateTotal(List.of(item));

        assertThat(total).isEqualByComparingTo("16.50");
    }
}