package org.example.backendlibrary.controllers;

import jakarta.validation.Valid;

import org.example.backendlibrary.dtos.requests.AddCopyToBorrowTicketRequest;
import org.example.backendlibrary.dtos.requests.BorrowTicketCreationRequest;
import org.example.backendlibrary.dtos.requests.BorrowTicketUpdateRequest;
import org.example.backendlibrary.dtos.responses.BorrowTicketResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.dtos.responses.Response;
import org.example.backendlibrary.services.BorrowTicketService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${app.api-prefix}/borrow-tickets")
@RequiredArgsConstructor
public class BorrowTicketController {
    private final BorrowTicketService borrowTicketService;

    @GetMapping
    public Response<PageResponse<BorrowTicketResponse>> getAllBorrowTickets(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return Response.<PageResponse<BorrowTicketResponse>>builder()
                .success(true)
                .data(borrowTicketService.getAll(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public Response<BorrowTicketResponse> getBorrowTicketById(@PathVariable Long id) {
        return Response.<BorrowTicketResponse>builder()
                .success(true)
                .data(borrowTicketService.getById(id))
                .build();
    }

    @PostMapping
    public Response<BorrowTicketResponse> createBorrowTicket(
            @RequestBody @Valid BorrowTicketCreationRequest borrowTicketCreationRequest) {
        return Response.<BorrowTicketResponse>builder()
                .success(true)
                .data(borrowTicketService.create(borrowTicketCreationRequest))
                .build();
    }

    @PostMapping("/{id}")
    public Response<BorrowTicketResponse> addCopyToBorrowTicket(
            @PathVariable Long id, @RequestBody @Valid AddCopyToBorrowTicketRequest addCopyToBorrowTicketRequest) {
        return Response.<BorrowTicketResponse>builder()
                .success(true)
                .data(borrowTicketService.addCopyToBorrowTicket(id, addCopyToBorrowTicketRequest))
                .build();
    }

    @PutMapping("/{id}")
    public Response<BorrowTicketResponse> updateBorrowTicket(
            @RequestBody @Valid BorrowTicketUpdateRequest borrowTicketUpdateRequest, @PathVariable Long id) {
        return Response.<BorrowTicketResponse>builder()
                .success(true)
                .data(borrowTicketService.update(id, borrowTicketUpdateRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteGenreById(@PathVariable Long id) {
        borrowTicketService.delete(id);

        return Response.<Void>builder().success(true).build();
    }
}
