package org.example.backendlibrary.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backendlibrary.dtos.requests.OrderCreationRequest;
import org.example.backendlibrary.dtos.requests.OrderUpdateRequest;
import org.example.backendlibrary.dtos.responses.OrderResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.dtos.responses.Response;
import org.example.backendlibrary.services.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api-prefix}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public Response<PageResponse<OrderResponse>> getAllGenres(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return Response.<PageResponse<OrderResponse>>builder()
                .success(true)
                .data(orderService.getAll(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public Response<OrderResponse> getGenreById(@PathVariable Long id) {
        return Response.<OrderResponse>builder()
                .success(true)
                .data(orderService.getById(id))
                .build();
    }

    @PostMapping
    public Response<OrderResponse> createGenre(@RequestBody @Valid OrderCreationRequest orderCreationRequest) {
        return Response.<OrderResponse>builder()
                .success(true)
                .data(orderService.create(orderCreationRequest))
                .build();
    }

    @PutMapping("/{id}")
    public Response<OrderResponse> updateGenre(@RequestBody OrderUpdateRequest orderUpdateRequest, @PathVariable Long id) {
        return Response.<OrderResponse>builder()
                .success(true)
                .data(orderService.update(id, orderUpdateRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteGenreById(@PathVariable Long id) {
        orderService.delete(id);

        return Response.<Void>builder().success(true).build();
    }
}
