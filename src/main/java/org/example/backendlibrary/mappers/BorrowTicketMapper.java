package org.example.backendlibrary.mappers;

import org.example.backendlibrary.dtos.requests.BorrowTicketCreationRequest;
import org.example.backendlibrary.dtos.requests.BorrowTicketUpdateRequest;
import org.example.backendlibrary.dtos.responses.BorrowTicketResponse;
import org.example.backendlibrary.entities.BorrowTicket;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BorrowTicketMapper {
    BorrowTicketResponse toBorrowTicketResponse(BorrowTicket borrowTicket);

    BorrowTicket toBorrowTicket(BorrowTicketCreationRequest borrowTicketCreationRequest);

    void updateBorrowTicket(
            @MappingTarget BorrowTicket borrowTicket, BorrowTicketUpdateRequest borrowTicketUpdateRequest);
}
