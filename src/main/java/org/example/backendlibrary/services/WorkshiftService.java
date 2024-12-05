package org.example.backendlibrary.services;

import lombok.RequiredArgsConstructor;
import org.example.backendlibrary.dtos.requests.WorkshiftCreationRequest;
import org.example.backendlibrary.dtos.requests.WorkshiftUpdateRequest;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.dtos.responses.WorkshiftResponse;
import org.example.backendlibrary.entities.Workshift;
import org.example.backendlibrary.exceptions.AppException;
import org.example.backendlibrary.exceptions.ErrorCode;
import org.example.backendlibrary.mappers.WorkshiftMapper;
import org.example.backendlibrary.repositories.WorkshiftRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkshiftService {
    private final WorkshiftRepository workshiftRepository;
    private final WorkshiftMapper workshiftMapper;

    public PageResponse<WorkshiftResponse> getAll(int page, int size) {
        List<Workshift> workshifts = workshiftRepository.findAll(page, size);

        long totalRecords = workshiftRepository.count();

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        return PageResponse.<WorkshiftResponse>builder()
                .items(workshifts.stream().map(workshiftMapper::toWorkshiftResponse).toList())
                .records(totalRecords)
                .totalPages(totalPages)
                .page(page)
                .build();
    }

    public WorkshiftResponse getById(Long id) {
        Optional<Workshift> optionalWorkshift = Optional.ofNullable(workshiftRepository.findById(id));

        if (optionalWorkshift.isEmpty()) {
            throw new AppException(ErrorCode.WORKSHIFT_NOTFOUND);
        }

        return workshiftMapper.toWorkshiftResponse(optionalWorkshift.get());
    }

    public WorkshiftResponse create(WorkshiftCreationRequest workshiftCreationRequest) {
        Workshift workshift = workshiftMapper.toWorkshift(workshiftCreationRequest);

        long orderId = workshiftRepository.save(workshift);
        workshift.setId(orderId);

        return workshiftMapper.toWorkshiftResponse(workshift);
    }

    public WorkshiftResponse update(long id, WorkshiftUpdateRequest workshiftUpdateRequest) {
        Optional<Workshift> optionalWorkshift = Optional.ofNullable(workshiftRepository.findById(id));

        if (optionalWorkshift.isEmpty()) {
            throw new AppException(ErrorCode.WORKSHIFT_NOTFOUND);
        }

        Workshift workshift = optionalWorkshift.get();

        workshiftMapper.updateWorkshift(workshift, workshiftUpdateRequest);

        workshiftRepository.update(workshift);

        return workshiftMapper.toWorkshiftResponse(workshift);
    }

    public void delete(Long id) {
        if (!workshiftRepository.existsById(id)) {
            throw new AppException(ErrorCode.WORKSHIFT_NOTFOUND);
        }

        workshiftRepository.deleteById(id);
    }
}
