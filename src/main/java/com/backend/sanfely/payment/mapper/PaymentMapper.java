package com.backend.sanfely.payment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import com.backend.sanfely.payment.domain.Payment;
import com.backend.sanfely.payment.dto.PaymentResponseDto;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
	 @Mapping(source = "order.id", target = "orderId")
	    PaymentResponseDto toResponseDto(Payment payment);
}
