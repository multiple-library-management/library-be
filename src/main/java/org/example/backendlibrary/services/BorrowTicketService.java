package org.example.backendlibrary.services;

import java.util.List;
import java.util.Optional;

import org.example.backendlibrary.dtos.requests.AddCopyToBorrowTicketRequest;
import org.example.backendlibrary.dtos.requests.BorrowTicketCreationRequest;
import org.example.backendlibrary.dtos.requests.BorrowTicketUpdateRequest;
import org.example.backendlibrary.dtos.responses.BorrowTicketResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.entities.BorrowTicket;
import org.example.backendlibrary.entities.CopyBorrowTicket;
import org.example.backendlibrary.exceptions.AppException;
import org.example.backendlibrary.exceptions.ErrorCode;
import org.example.backendlibrary.mappers.BorrowTicketMapper;
import org.example.backendlibrary.mappers.CopyBorrowTicketMapper;
import org.example.backendlibrary.repositories.BorrowTicketRepository;
import org.example.backendlibrary.repositories.CopyBorrowTicketRepository;
import org.example.backendlibrary.repositories.CopyRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BorrowTicketService {
    // repositories
    private final BorrowTicketRepository borrowTicketRepository;
    private final CopyBorrowTicketRepository copyBorrowTicketRepository;
    private final CopyRepository copyRepository;

    // mappers
    private final BorrowTicketMapper borrowTicketMapper;
    private final CopyBorrowTicketMapper copyBorrowTicketMapper;

    public PageResponse<BorrowTicketResponse> getAll(int page, int size) {
        List<BorrowTicket> borrowTickets = borrowTicketRepository.findAll(page, size);

        // map to response
        List<BorrowTicketResponse> borrowTicketResponses = borrowTickets.stream()
                .map(borrowTicketMapper::toBorrowTicketResponse)
                .toList();

        // find copies and add to response
        borrowTicketResponses.forEach(borrowTicketResponse -> {
            List<CopyBorrowTicket> copyBorrowTickets =
                    copyBorrowTicketRepository.findAllByBorrowTicketId(borrowTicketResponse.getId());

            borrowTicketResponse.setCopies(copyBorrowTickets.stream()
                    .map(copyBorrowTicketMapper::toCopyBorrowTicketResponse)
                    .toList());
        });

        long totalRecords = borrowTicketRepository.count();

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        return PageResponse.<BorrowTicketResponse>builder()
                .items(borrowTicketResponses)
                .records(totalRecords)
                .totalPages(totalPages)
                .page(page)
                .build();
    }

    public BorrowTicketResponse getById(Long id) {
        Optional<BorrowTicket> optionalBorrowTicket = Optional.ofNullable(borrowTicketRepository.findById(id));

        if (optionalBorrowTicket.isEmpty()) {
            throw new AppException(ErrorCode.BORROW_TICKET_NOTFOUND);
        }

        BorrowTicketResponse borrowTicketResponse =
                borrowTicketMapper.toBorrowTicketResponse(optionalBorrowTicket.get());
        List<CopyBorrowTicket> copyBorrowTickets =
                copyBorrowTicketRepository.findAllByBorrowTicketId(borrowTicketResponse.getId());
        borrowTicketResponse.setCopies(copyBorrowTickets.stream()
                .map(copyBorrowTicketMapper::toCopyBorrowTicketResponse)
                .toList());

        return borrowTicketResponse;
    }

    public BorrowTicketResponse create(BorrowTicketCreationRequest borrowTicketCreationRequest) {
        BorrowTicket borrowTicket = borrowTicketMapper.toBorrowTicket(borrowTicketCreationRequest);

        long borrowTicketId = borrowTicketRepository.save(borrowTicket);
        borrowTicket.setId(borrowTicketId);

        return borrowTicketMapper.toBorrowTicketResponse(borrowTicket);
    }

    public BorrowTicketResponse addCopyToBorrowTicket(
            Long borrowTicketId, AddCopyToBorrowTicketRequest addCopyToBorrowTicketRequest) {
        // check if the borrow ticket exists
        if (!borrowTicketRepository.existsById(borrowTicketId)) {
            throw new AppException(ErrorCode.BORROW_TICKET_NOTFOUND);
        }

        // check if copy exists
        if (!copyRepository.existsById(addCopyToBorrowTicketRequest.getCopyId())) {
            throw new AppException(ErrorCode.COPY_NOTFOUND);
        }

        // get borrow ticket reponse
        BorrowTicketResponse borrowTicketResponse =
                borrowTicketMapper.toBorrowTicketResponse(borrowTicketRepository.findById(borrowTicketId));

        // get current list of copies borrowed in the ticket
        List<CopyBorrowTicket> copyBorrowTickets = copyBorrowTicketRepository.findAllByBorrowTicketId(borrowTicketId);

        // save the copy want to borrow
        CopyBorrowTicket copyBorrowTicket = copyBorrowTicketMapper.toCopyBorrowTicket(addCopyToBorrowTicketRequest);
        copyBorrowTicket.setBorrowTicketId(borrowTicketId);

        copyBorrowTicketRepository.save(copyBorrowTicket);

        // add back to the borrow ticket response
        copyBorrowTickets.add(copyBorrowTicket);
        borrowTicketResponse.setCopies(copyBorrowTickets.stream()
                .map(copyBorrowTicketMapper::toCopyBorrowTicketResponse)
                .toList());

        return borrowTicketResponse;
    }

    public BorrowTicketResponse update(long id, BorrowTicketUpdateRequest borrowTicketUpdateRequest) {
        Optional<BorrowTicket> optionalBorrowTicket = Optional.ofNullable(borrowTicketRepository.findById(id));

        if (optionalBorrowTicket.isEmpty()) {
            throw new AppException(ErrorCode.BORROW_TICKET_NOTFOUND);
        }

        BorrowTicket transfer = optionalBorrowTicket.get();

        borrowTicketMapper.updateBorrowTicket(transfer, borrowTicketUpdateRequest);

        borrowTicketRepository.update(transfer);

        return borrowTicketMapper.toBorrowTicketResponse(transfer);
    }

    public void delete(Long id) {
        if (!borrowTicketRepository.existsById(id)) {
            throw new AppException(ErrorCode.BORROW_TICKET_NOTFOUND);
        }

        borrowTicketRepository.deleteById(id);
    }
}
