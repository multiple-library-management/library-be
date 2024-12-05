package org.example.backendlibrary.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.backendlibrary.dtos.requests.LibraryCreationRequest;
import org.example.backendlibrary.dtos.requests.LibraryUpdateRequest;
import org.example.backendlibrary.dtos.requests.WarehouseCreationRequest;
import org.example.backendlibrary.dtos.requests.WarehouseUpdateRequest;
import org.example.backendlibrary.dtos.responses.LibraryResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.dtos.responses.Response;
import org.example.backendlibrary.dtos.responses.WarehouseResponse;
import org.example.backendlibrary.services.LibraryService;
import org.example.backendlibrary.services.WarehouseService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api-prefix}/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;

    @GetMapping
    public Response<PageResponse<WarehouseResponse>> getAllWarehouses(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return Response.<PageResponse<WarehouseResponse>>builder()
                .success(true)
                .data(warehouseService.getAll(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public Response<WarehouseResponse> getLibraryById(@PathVariable Long id) {
        return Response.<WarehouseResponse>builder()
                .success(true)
                .data(warehouseService.getById(id))
                .build();
    }

    @PostMapping
    public Response<WarehouseResponse> createGenre(@RequestBody @Valid WarehouseCreationRequest warehouseCreationRequest) {
        return Response.<WarehouseResponse>builder()
                .success(true)
                .data(warehouseService.create(warehouseCreationRequest))
                .build();
    }

    @PutMapping("/{id}")
    public Response<WarehouseResponse> updateGenre(@RequestBody WarehouseUpdateRequest warehouseUpdateRequest, @PathVariable Long id) {
        return Response.<WarehouseResponse>builder()
                .success(true)
                .data(warehouseService.update(id, warehouseUpdateRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteGenreById(@PathVariable Long id) {
        warehouseService.delete(id);

        return Response.<Void>builder().success(true).build();
    }
}
