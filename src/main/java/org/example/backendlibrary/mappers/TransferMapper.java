package org.example.backendlibrary.mappers;

import org.example.backendlibrary.dtos.requests.TransferCreationRequest;
import org.example.backendlibrary.dtos.requests.TransferUpdateRequest;
import org.example.backendlibrary.dtos.responses.TransferResponse;
import org.example.backendlibrary.entities.Transfer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransferMapper {
    TransferResponse toTransferResponse(Transfer transfer);

    Transfer toTransfer(TransferCreationRequest transferCreationRequest);

    void updateTransfer(@MappingTarget Transfer transfer, TransferUpdateRequest transferUpdateRequest);
}
