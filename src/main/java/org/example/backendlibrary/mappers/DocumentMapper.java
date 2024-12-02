package org.example.backendlibrary.mappers;

import org.example.backendlibrary.dtos.requests.DocumentCreationRequest;
import org.example.backendlibrary.dtos.requests.DocumentUpdateRequest;
import org.example.backendlibrary.dtos.responses.DocumentResponse;
import org.example.backendlibrary.entities.Document;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DocumentMapper {
    DocumentResponse toDocumentResponse(Document document);

    Document toDocument(DocumentCreationRequest documentCreationRequest);

    void updateDocument(@MappingTarget Document document, DocumentUpdateRequest documentUpdateRequest);
}
