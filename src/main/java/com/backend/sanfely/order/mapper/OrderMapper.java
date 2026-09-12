package com.backend.sanfely.order.mapper;

import com.backend.sanfely.order.domain.Order;
import com.backend.sanfely.order.domain.OrderItem;
import com.backend.sanfely.order.dto.OrderItemResponseDto;
import com.backend.sanfely.order.dto.OrderResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "traiteur.id", target = "traiteurId")
    OrderResponseDto toResponseDto(Order order);

    @Mapping(source = "dish.id", target = "dishId")
    @Mapping(source = "dish.name", target = "dishName")
    OrderItemResponseDto toResponseDto(OrderItem item);
}