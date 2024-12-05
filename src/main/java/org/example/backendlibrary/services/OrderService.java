package org.example.backendlibrary.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.example.backendlibrary.dtos.requests.OrderCreationRequest;
import org.example.backendlibrary.dtos.requests.OrderUpdateRequest;
import org.example.backendlibrary.dtos.responses.OrderResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.entities.Order;
import org.example.backendlibrary.exceptions.AppException;
import org.example.backendlibrary.exceptions.ErrorCode;
import org.example.backendlibrary.mappers.OrderMapper;
import org.example.backendlibrary.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public PageResponse<OrderResponse> getAll(int page, int size) {
        List<Order> orders = orderRepository.findAll(page, size);

        long totalRecords = orderRepository.count();

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        return PageResponse.<OrderResponse>builder()
                .items(orders.stream().map(orderMapper::toOrderResponse).toList())
                .records(totalRecords)
                .totalPages(totalPages)
                .page(page)
                .build();
    }

    public OrderResponse getById(Long id) {
        Optional<Order> optionalOrder = Optional.ofNullable(orderRepository.findById(id));

        if (optionalOrder.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_NOTFOUND);
        }

        return orderMapper.toOrderResponse(optionalOrder.get());
    }

    public OrderResponse create(OrderCreationRequest orderCreationRequest) {
        Order order = orderMapper.toOrder(orderCreationRequest);
        order.setCreatedDate(Timestamp.from(Instant.now()));
        order.setTotalPrice(0);

        long orderId = orderRepository.save(order);
        order.setId(orderId);

        return orderMapper.toOrderResponse(order);
    }

    public OrderResponse update(long id, OrderUpdateRequest orderUpdateRequest) {
        Optional<Order> optionalOrder = Optional.ofNullable(orderRepository.findById(id));

        if (optionalOrder.isEmpty()) {
            throw new AppException(ErrorCode.ORDER_NOTFOUND);
        }

        Order order = optionalOrder.get();

        orderMapper.updateOrder(order, orderUpdateRequest);

        orderRepository.update(order);

        return orderMapper.toOrderResponse(order);
    }

    public void delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new AppException(ErrorCode.ORDER_NOTFOUND);
        }

        orderRepository.deleteById(id);
    }
}
