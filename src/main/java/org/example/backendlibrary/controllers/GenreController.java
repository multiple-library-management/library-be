package org.example.backendlibrary.controllers;

import jakarta.validation.Valid;

import org.example.backendlibrary.dtos.requests.GenreCreationRequest;
import org.example.backendlibrary.dtos.requests.GenreUpdateRequest;
import org.example.backendlibrary.dtos.responses.GenreResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.dtos.responses.Response;
import org.example.backendlibrary.services.GenreService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${app.api-prefix}/genres")
@RequiredArgsConstructor
public class GenreController {
    private final GenreService genreService;

    @GetMapping
    public Response<PageResponse<GenreResponse>> getAllGenres(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return Response.<PageResponse<GenreResponse>>builder()
                .success(true)
                .data(genreService.getAll(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public Response<GenreResponse> getGenreById(@PathVariable Long id) {
        return Response.<GenreResponse>builder()
                .success(true)
                .data(genreService.getById(id))
                .build();
    }

    @PostMapping
    public Response<GenreResponse> createGenre(@RequestBody @Valid GenreCreationRequest genreCreationRequest) {
        return Response.<GenreResponse>builder()
                .success(true)
                .data(genreService.create(genreCreationRequest))
                .build();
    }

    @PutMapping("/{id}")
    public Response<GenreResponse> updateGenre(@RequestBody GenreUpdateRequest genreUpdateRequest, @PathVariable Long id) {

        return Response.<GenreResponse>builder()
                .success(true)
                .data(genreService.update(id, genreUpdateRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteGenreById(@PathVariable Long id) {
        genreService.delete(id);

        return Response.<Void>builder().success(true).build();
    }
}
