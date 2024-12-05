package org.example.backendlibrary.mappers;

import org.example.backendlibrary.dtos.requests.AddCopyToTransferRequest;
import org.example.backendlibrary.dtos.responses.CopyTransferResponse;
import org.example.backendlibrary.entities.CopyTransfer;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CopyTransferMapper {
    CopyTransferResponse toCopyBorrowTicketResponse(CopyTransfer copyTransfer);

    CopyTransfer toCopyTransfer(AddCopyToTransferRequest addCopyToTransferRequest);
}
