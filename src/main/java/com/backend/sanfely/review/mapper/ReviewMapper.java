package com.backend.sanfely.review.mapper;

import com.backend.sanfely.review.domain.Review;
import com.backend.sanfely.review.dto.ReviewResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "client.id", target = "clientId")
    ReviewResponseDto toResponseDto(Review review);
}