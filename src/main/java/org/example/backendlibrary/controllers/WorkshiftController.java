package org.example.backendlibrary.controllers;

import jakarta.validation.Valid;

import org.example.backendlibrary.dtos.requests.WorkshiftCreationRequest;
import org.example.backendlibrary.dtos.requests.WorkshiftUpdateRequest;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.dtos.responses.Response;
import org.example.backendlibrary.dtos.responses.WorkshiftResponse;
import org.example.backendlibrary.services.WorkshiftService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${app.api-prefix}/workshifts")
@RequiredArgsConstructor
public class WorkshiftController {
    private final WorkshiftService workshiftService;

    @GetMapping
    public Response<PageResponse<WorkshiftResponse>> getAllGenres(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return Response.<PageResponse<WorkshiftResponse>>builder()
                .success(true)
                .data(workshiftService.getAll(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public Response<WorkshiftResponse> getGenreById(@PathVariable Long id) {
        return Response.<WorkshiftResponse>builder()
                .success(true)
                .data(workshiftService.getById(id))
                .build();
    }

    @PostMapping
    public Response<WorkshiftResponse> createGenre(
            @RequestBody @Valid WorkshiftCreationRequest workshiftCreationRequest) {
        return Response.<WorkshiftResponse>builder()
                .success(true)
                .data(workshiftService.create(workshiftCreationRequest))
                .build();
    }

    @PutMapping("/{id}")
    public Response<WorkshiftResponse> updateGenre(
            @RequestBody @Valid WorkshiftUpdateRequest orderUpdateRequest, @PathVariable Long id) {
        return Response.<WorkshiftResponse>builder()
                .success(true)
                .data(workshiftService.update(id, orderUpdateRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteGenreById(@PathVariable Long id) {
        workshiftService.delete(id);

        return Response.<Void>builder().success(true).build();
    }
}
