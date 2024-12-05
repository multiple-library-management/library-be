package org.example.backendlibrary.mappers;

import org.example.backendlibrary.dtos.requests.OrderCreationRequest;
import org.example.backendlibrary.dtos.requests.OrderUpdateRequest;
import org.example.backendlibrary.dtos.responses.OrderResponse;
import org.example.backendlibrary.entities.Order;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderMapper {
    OrderResponse toOrderResponse(Order order);

    Order toOrder(OrderCreationRequest orderCreationRequest);

    void updateOrder(@MappingTarget Order order, OrderUpdateRequest orderUpdateRequest);
}
