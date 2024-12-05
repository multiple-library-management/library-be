package org.example.backendlibrary.controllers;

import jakarta.validation.Valid;

import org.example.backendlibrary.dtos.requests.AddCopyToTransferRequest;
import org.example.backendlibrary.dtos.requests.TransferCreationRequest;
import org.example.backendlibrary.dtos.requests.TransferUpdateRequest;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.dtos.responses.Response;
import org.example.backendlibrary.dtos.responses.TransferResponse;
import org.example.backendlibrary.services.TransferService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${app.api-prefix}/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @GetMapping
    public Response<PageResponse<TransferResponse>> getAllGenres(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return Response.<PageResponse<TransferResponse>>builder()
                .success(true)
                .data(transferService.getAll(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public Response<TransferResponse> getGenreById(@PathVariable Long id) {
        return Response.<TransferResponse>builder()
                .success(true)
                .data(transferService.getById(id))
                .build();
    }

    @PostMapping
    public Response<TransferResponse> createGenre(@RequestBody @Valid TransferCreationRequest transferCreationRequest) {
        return Response.<TransferResponse>builder()
                .success(true)
                .data(transferService.create(transferCreationRequest))
                .build();
    }

    @PostMapping("/{id}")
    public Response<TransferResponse> updateGenre(
            @PathVariable Long id, @RequestBody @Valid AddCopyToTransferRequest addCopyToTransferRequest) {
        return Response.<TransferResponse>builder()
                .success(true)
                .data(transferService.addCopyToTransfer(id, addCopyToTransferRequest))
                .build();

    }

    @PutMapping("/{id}")
    public Response<TransferResponse> updateGenre(
            @RequestBody @Valid TransferUpdateRequest transferUpdateRequest, @PathVariable Long id) {
        return Response.<TransferResponse>builder()
                .success(true)
                .data(transferService.update(id, transferUpdateRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteGenreById(@PathVariable Long id) {
        transferService.delete(id);

        return Response.<Void>builder().success(true).build();
    }
}
