package org.example.backendlibrary.mappers;

import org.example.backendlibrary.dtos.requests.CopyCreationRequest;
import org.example.backendlibrary.dtos.requests.CopyUpdateRequest;
import org.example.backendlibrary.dtos.responses.CopyResponse;
import org.example.backendlibrary.entities.Copy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CopyMapper {
    CopyResponse toCopyResponse(Copy copy);

    Copy toCopy(CopyCreationRequest copyCreationRequest);

    void updateCopy(@MappingTarget Copy copy, CopyUpdateRequest copyUpdateRequest);
}
