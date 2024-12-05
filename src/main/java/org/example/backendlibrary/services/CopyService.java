package org.example.backendlibrary.services;

import java.util.List;
import java.util.Optional;

import org.example.backendlibrary.dtos.requests.CopyCreationRequest;
import org.example.backendlibrary.dtos.requests.CopyUpdateRequest;
import org.example.backendlibrary.dtos.responses.CopyResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.entities.Copy;
import org.example.backendlibrary.exceptions.AppException;
import org.example.backendlibrary.exceptions.ErrorCode;
import org.example.backendlibrary.mappers.CopyMapper;
import org.example.backendlibrary.repositories.CopyRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CopyService {
    private final CopyRepository copyRepository;
    private final CopyMapper copyMapper;

    public PageResponse<CopyResponse> getAll(int page, int size) {
        List<Copy> copies = copyRepository.findAll(page, size);

        long totalRecords = copyRepository.count();

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        return PageResponse.<CopyResponse>builder()
                .items(copies.stream().map(copyMapper::toCopyResponse).toList())
                .records(totalRecords)
                .totalPages(totalPages)
                .page(page)
                .build();
    }

    public CopyResponse getById(Long id) {
        Optional<Copy> optionalCopy = Optional.ofNullable(copyRepository.findById(id));

        if (optionalCopy.isEmpty()) {
            throw new AppException(ErrorCode.COPY_NOTFOUND);
        }

        return copyMapper.toCopyResponse(optionalCopy.get());
    }

    public CopyResponse create(CopyCreationRequest copyCreationRequest) {
        Copy copy = copyMapper.toCopy(copyCreationRequest);

        if (copy.getLibraryId() == 0) {
            copy.setLibraryId(null);
        } else if (copy.getWarehouseId() == 0) {
            copy.setWarehouseId(null);
        }

        long copyId = copyRepository.save(copy);
        copy.setId(copyId);

        return copyMapper.toCopyResponse(copy);
    }

    public CopyResponse update(long id, CopyUpdateRequest copyUpdateRequest) {
        Optional<Copy> optionalCopy = Optional.ofNullable(copyRepository.findById(id));

        if (optionalCopy.isEmpty()) {
            throw new AppException(ErrorCode.COPY_NOTFOUND);
        }

        Copy copy = optionalCopy.get();

        log.info(String.valueOf(copy));

        copyMapper.updateCopy(copy, copyUpdateRequest);

        if (copy.getLibraryId() == 0) {
            copy.setLibraryId(null);
        } else if (copy.getWarehouseId() == 0) {
            copy.setWarehouseId(null);
        }

        copyRepository.update(copy);

        return copyMapper.toCopyResponse(copy);
    }

    public void delete(Long id) {
        if (!copyRepository.existsById(id)) {
            throw new AppException(ErrorCode.COPY_NOTFOUND);
        }

        copyRepository.deleteById(id);
    }
}
