package com.lib.delivery.mapper;

import com.lib.delivery.dto.request.OrderRequest;
import com.lib.delivery.dto.response.OrderResponse;
import com.lib.delivery.entity.Order;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING
)
public interface OrderMapper {

    Order map(OrderRequest orderRequest);

    OrderResponse map(Order order);

}
