package org.example.backendlibrary.controllers;

import jakarta.validation.Valid;

import org.example.backendlibrary.dtos.requests.LibraryCreationRequest;
import org.example.backendlibrary.dtos.requests.LibraryUpdateRequest;
import org.example.backendlibrary.dtos.responses.LibraryResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.dtos.responses.Response;
import org.example.backendlibrary.services.LibraryService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${app.api-prefix}/libraries")
@RequiredArgsConstructor
public class LibraryController {
    private final LibraryService libraryService;

    @GetMapping
    public Response<PageResponse<LibraryResponse>> getAllLibraries(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return Response.<PageResponse<LibraryResponse>>builder()
                .success(true)
                .data(libraryService.getAll(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public Response<LibraryResponse> getLibraryById(@PathVariable Long id) {
        return Response.<LibraryResponse>builder()
                .success(true)
                .data(libraryService.getById(id))
                .build();
    }

    @PostMapping
    public Response<LibraryResponse> createGenre(@RequestBody @Valid LibraryCreationRequest libraryCreationRequest) {
        return Response.<LibraryResponse>builder()
                .success(true)
                .data(libraryService.create(libraryCreationRequest))
                .build();
    }

    @PutMapping("/{id}")
    public Response<LibraryResponse> updateGenre(
            @RequestBody @Valid LibraryUpdateRequest libraryUpdateRequest, @PathVariable Long id) {
        return Response.<LibraryResponse>builder()
                .success(true)
                .data(libraryService.update(id, libraryUpdateRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteGenreById(@PathVariable Long id) {
        libraryService.delete(id);

        return Response.<Void>builder().success(true).build();
    }
}
