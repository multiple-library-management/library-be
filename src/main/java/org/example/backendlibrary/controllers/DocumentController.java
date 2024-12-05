package org.example.backendlibrary.controllers;

import jakarta.validation.Valid;

import org.example.backendlibrary.dtos.requests.DocumentCreationRequest;
import org.example.backendlibrary.dtos.requests.DocumentUpdateRequest;
import org.example.backendlibrary.dtos.responses.DocumentResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.dtos.responses.Response;
import org.example.backendlibrary.services.DocumentService;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${app.api-prefix}/documents")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @GetMapping
    public Response<PageResponse<DocumentResponse>> getAllDocuments(
            @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
        return Response.<PageResponse<DocumentResponse>>builder()
                .success(true)
                .data(documentService.getAll(page, size))
                .build();
    }

    @GetMapping("/{id}")
    public Response<DocumentResponse> getDocumentById(@PathVariable Long id) {
        return Response.<DocumentResponse>builder()
                .success(true)
                .data(documentService.getById(id))
                .build();
    }

    @PostMapping
    public Response<DocumentResponse> createDocument(@RequestBody @Valid DocumentCreationRequest documentCreationRequest) {
        return Response.<DocumentResponse>builder()
                .success(true)
                .data(documentService.create(documentCreationRequest))
                .build();
    }

    @PutMapping("/{id}")
    public Response<DocumentResponse> updateDocument(
            @RequestBody DocumentUpdateRequest documentUpdateRequest, @PathVariable Long id) {
        return Response.<DocumentResponse>builder()
                .success(true)
                .data(documentService.update(id, documentUpdateRequest))
                .build();
    }

    @DeleteMapping("/{id}")
    public Response<Void> deleteDocument(@PathVariable Long id) {
        documentService.delete(id);

        return Response.<Void>builder().success(true).build();
    }
}
