package org.example.backendlibrary.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.example.backendlibrary.dtos.requests.AddCopyToTransferRequest;
import org.example.backendlibrary.dtos.requests.TransferCreationRequest;
import org.example.backendlibrary.dtos.requests.TransferUpdateRequest;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.dtos.responses.TransferResponse;
import org.example.backendlibrary.entities.CopyTransfer;
import org.example.backendlibrary.entities.Transfer;
import org.example.backendlibrary.exceptions.AppException;
import org.example.backendlibrary.exceptions.ErrorCode;
import org.example.backendlibrary.mappers.CopyTransferMapper;
import org.example.backendlibrary.mappers.TransferMapper;
import org.example.backendlibrary.repositories.CopyRepository;
import org.example.backendlibrary.repositories.CopyTransferRepository;
import org.example.backendlibrary.repositories.TransferRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final TransferRepository transferRepository;
    private final CopyTransferRepository copyTransferRepository;
    private final CopyRepository copyRepository;

    private final TransferMapper transferMapper;
    private final CopyTransferMapper copyTransferMapper;

    public PageResponse<TransferResponse> getAll(int page, int size) {
        List<Transfer> transfers = transferRepository.findAll(page, size);

        List<TransferResponse> transferResponses =
                transfers.stream().map(transferMapper::toTransferResponse).toList();

        transferResponses.forEach(transferResponse -> {
            List<CopyTransfer> copyTransfers = copyTransferRepository.findAllByTransferId(transferResponse.getId());

            List<Long> copyTransferResponses =
                    copyTransfers.stream().map(CopyTransfer::getCopyId).toList();

            transferResponse.setCopies(copyTransferResponses);
        });

        long totalRecords = transferRepository.count();

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        return PageResponse.<TransferResponse>builder()
                .items(transferResponses)
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

        // get transfer
        Transfer transfer = optionalTransfer.get();
        // map to transfer response
        TransferResponse transferResponse = transferMapper.toTransferResponse(transfer);
        // get its copies
        List<CopyTransfer> copyTransfers = copyTransferRepository.findAllByTransferId(transferResponse.getId());
        // map to long
        List<Long> copyTransferResponses =
                copyTransfers.stream().map(CopyTransfer::getCopyId).toList();
        // set the list
        transferResponse.setCopies(copyTransferResponses);
        return transferResponse;
    }

    public TransferResponse create(TransferCreationRequest transferCreationRequest) {
        Transfer transfer = transferMapper.toTransfer(transferCreationRequest);

        if (transferCreationRequest.getCreatedDate() == null) {
            transfer.setCreatedDate(Timestamp.from(Instant.now()));
        }

        long transferId = transferRepository.save(transfer);
        transfer.setId(transferId);

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

    public TransferResponse addCopyToTransfer(Long id, AddCopyToTransferRequest addCopyToTransferRequest) {
        // get the transfer
        Optional<Transfer> optionalTransfer = Optional.ofNullable(transferRepository.findById(id));

        if (optionalTransfer.isEmpty()) {
            throw new AppException(ErrorCode.TRANSFER_NOTFOUND);
        }

        Transfer transfer = optionalTransfer.get();

        // add copy to transfer
        // validate the existence of copy
        if (!copyRepository.existsById(addCopyToTransferRequest.getCopyId())) {
            throw new AppException(ErrorCode.COPY_NOTFOUND);
        }

        CopyTransfer copyTransfer = copyTransferMapper.toCopyTransfer(addCopyToTransferRequest);
        copyTransfer.setTransferId(transfer.getId());

        copyTransferRepository.save(copyTransfer);

        // get the current copy list of current transfer
        List<CopyTransfer> copyTransfers = copyTransferRepository.findAllByTransferId(transfer.getId());

        // create transfer response
        TransferResponse transferResponse = transferMapper.toTransferResponse(transfer);

        // map to copy id list
        transferResponse.setCopies(
                copyTransfers.stream().map(CopyTransfer::getCopyId).toList());

        return transferResponse;
    }
}
