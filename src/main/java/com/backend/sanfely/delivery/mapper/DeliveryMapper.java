package com.backend.sanfely.delivery.mapper;

import com.backend.sanfely.delivery.domain.DeliveryCompany;
import com.backend.sanfely.delivery.domain.DeliveryOffer;
import com.backend.sanfely.delivery.domain.Livreur;
import com.backend.sanfely.delivery.dto.AdminDeliveryCompanyResponseDto;
import com.backend.sanfely.delivery.dto.AdminLivreurResponseDto;
import com.backend.sanfely.delivery.dto.DeliveryCompanyResponseDto;
import com.backend.sanfely.delivery.dto.DeliveryOfferResponseDto;
import com.backend.sanfely.delivery.dto.LivreurResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {

    DeliveryCompanyResponseDto toResponseDto(DeliveryCompany company);

    @Mapping(source = "deliveryCompany.id", target = "deliveryCompanyId")
    LivreurResponseDto toResponseDto(Livreur livreur);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.deliveryAddress", target = "deliveryAddress")
    DeliveryOfferResponseDto toResponseDto(DeliveryOffer offer);
    @Mapping(source = "deliveryCompany.id", target = "deliveryCompanyId")
    AdminLivreurResponseDto toAdminResponseDto(Livreur livreur);
    
    AdminDeliveryCompanyResponseDto toAdminResponseDto(DeliveryCompany company);
}