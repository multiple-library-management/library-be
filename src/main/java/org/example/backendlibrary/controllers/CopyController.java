package org.example.backendlibrary.controllers;

import jakarta.validation.Valid;

import org.example.backendlibrary.dtos.requests.CopyCreationRequest;
import org.example.backendlibrary.dtos.requests.CopyUpdateRequest;
import org.example.backendlibrary.dtos.responses.CopyResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.dtos.responses.Response;
import org.example.backendlibrary.services.CopyService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${app.api-prefix}/copies")
@RequiredArgsConstructor
public class CopyController {
    private final CopyService copyService;

    @GetMapping
    public Response<PageResponse<CopyResponse>> getAllGenres(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return Response.<PageResponse<CopyResponse>>builder()
                .success(true)
                .data(copyService.getAll(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public Response<CopyResponse> getGenreById(@PathVariable Long id) {
        return Response.<CopyResponse>builder()
                .success(true)
                .data(copyService.getById(id))
                .build();
    }

    @PostMapping
    public Response<CopyResponse> createGenre(@RequestBody @Valid CopyCreationRequest copyCreationRequest) {
        return Response.<CopyResponse>builder()
                .success(true)
                .data(copyService.create(copyCreationRequest))
                .build();
    }

    @PutMapping("/{id}")
    public Response<CopyResponse> updateGenre(
            @RequestBody @Valid CopyUpdateRequest copyUpdateRequest, @PathVariable Long id) {
        return Response.<CopyResponse>builder()
                .success(true)
                .data(copyService.update(id, copyUpdateRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteGenreById(@PathVariable Long id) {
        copyService.delete(id);

        return Response.<Void>builder().success(true).build();
    }
}
