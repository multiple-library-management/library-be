package org.example.backendlibrary.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.example.backendlibrary.dtos.requests.TransferCreationRequest;
import org.example.backendlibrary.dtos.requests.TransferUpdateRequest;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.dtos.responses.TransferResponse;
import org.example.backendlibrary.entities.Transfer;
import org.example.backendlibrary.exceptions.AppException;
import org.example.backendlibrary.exceptions.ErrorCode;
import org.example.backendlibrary.mappers.TransferMapper;
import org.example.backendlibrary.repositories.TransferRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final TransferRepository transferRepository;
    private final TransferMapper transferMapper;

    public PageResponse<TransferResponse> getAll(int page, int size) {
        List<Transfer> transfers = transferRepository.findAll(page, size);

        long totalRecords = transferRepository.count();

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        return PageResponse.<TransferResponse>builder()
                .items(transfers.stream()
                        .map(transferMapper::toTransferResponse)
                        .toList())
                .records(totalRecords)
                .totalPages(totalPages)
                .page(page)
                .build();
    }

    public TransferResponse getById(Long id) {
        Optional<Transfer> optionalTransfer = Optional.ofNullable(transferRepository.findById(id));

        if (optionalTransfer.isEmpty()) {
            throw new AppException(ErrorCode.TRANSFER_NOTFOUND);
        }

        return transferMapper.toTransferResponse(optionalTransfer.get());
    }

    public TransferResponse create(TransferCreationRequest transferCreationRequest) {
        Transfer transfer = transferMapper.toTransfer(transferCreationRequest);
        transfer.setCreatedDate(Timestamp.from(Instant.now()));

        long orderId = transferRepository.save(transfer);
        transfer.setId(orderId);

        return transferMapper.toTransferResponse(transfer);
    }

    public TransferResponse update(long id, TransferUpdateRequest transferUpdateRequest) {
        Optional<Transfer> optionalTransfer = Optional.ofNullable(transferRepository.findById(id));

        if (optionalTransfer.isEmpty()) {
            throw new AppException(ErrorCode.TRANSFER_NOTFOUND);
        }

        Transfer transfer = optionalTransfer.get();

        transferMapper.updateTransfer(transfer, transferUpdateRequest);

        transferRepository.update(transfer);

        return transferMapper.toTransferResponse(transfer);
    }

    public void delete(Long id) {
        if (!transferRepository.existsById(id)) {
            throw new AppException(ErrorCode.TRANSFER_NOTFOUND);
        }

        transferRepository.deleteById(id);
    }
}
