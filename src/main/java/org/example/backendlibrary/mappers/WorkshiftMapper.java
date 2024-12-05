package org.example.backendlibrary.mappers;

import org.example.backendlibrary.dtos.requests.WorkshiftCreationRequest;
import org.example.backendlibrary.dtos.requests.WorkshiftUpdateRequest;
import org.example.backendlibrary.dtos.responses.WorkshiftResponse;
import org.example.backendlibrary.entities.Workshift;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WorkshiftMapper {
    WorkshiftResponse toWorkshiftResponse(Workshift workshift);

    Workshift toWorkshift(WorkshiftCreationRequest workshiftCreationRequest);

    void updateWorkshift(@MappingTarget Workshift workshift, WorkshiftUpdateRequest workshiftUpdateRequest);
}
