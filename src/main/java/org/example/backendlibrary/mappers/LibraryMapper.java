package org.example.backendlibrary.mappers;

import org.example.backendlibrary.dtos.requests.LibraryCreationRequest;
import org.example.backendlibrary.dtos.requests.LibraryUpdateRequest;
import org.example.backendlibrary.dtos.responses.LibraryResponse;
import org.example.backendlibrary.entities.Library;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface LibraryMapper {
    LibraryResponse toLibraryResponse(Library library);

    Library toLibrary(LibraryCreationRequest libraryCreationRequest);

    void updateLibrary(@MappingTarget Library library, LibraryUpdateRequest libraryUpdateRequest);
}
