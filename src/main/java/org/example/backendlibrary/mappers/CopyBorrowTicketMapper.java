package org.example.backendlibrary.mappers;

import org.example.backendlibrary.dtos.requests.AddCopyToBorrowTicketRequest;
import org.example.backendlibrary.dtos.responses.CopyBorrowTicketResponse;
import org.example.backendlibrary.entities.CopyBorrowTicket;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CopyBorrowTicketMapper {
    CopyBorrowTicketResponse toCopyBorrowTicketResponse(CopyBorrowTicket copyBorrowTicket);

    CopyBorrowTicket toCopyBorrowTicket(AddCopyToBorrowTicketRequest addCopyToBorrowTicketRequest);
}
