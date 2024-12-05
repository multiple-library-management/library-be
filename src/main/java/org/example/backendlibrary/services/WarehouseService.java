package org.example.backendlibrary.services;

import lombok.RequiredArgsConstructor;
import org.example.backendlibrary.dtos.requests.LibraryCreationRequest;
import org.example.backendlibrary.dtos.requests.LibraryUpdateRequest;
import org.example.backendlibrary.dtos.requests.WarehouseCreationRequest;
import org.example.backendlibrary.dtos.requests.WarehouseUpdateRequest;
import org.example.backendlibrary.dtos.responses.LibraryResponse;
import org.example.backendlibrary.dtos.responses.PageResponse;
import org.example.backendlibrary.dtos.responses.WarehouseResponse;
import org.example.backendlibrary.entities.Library;
import org.example.backendlibrary.entities.Warehouse;
import org.example.backendlibrary.exceptions.AppException;
import org.example.backendlibrary.exceptions.ErrorCode;
import org.example.backendlibrary.mappers.LibraryMapper;
import org.example.backendlibrary.mappers.WarehouseMapper;
import org.example.backendlibrary.repositories.LibraryRepository;
import org.example.backendlibrary.repositories.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    private final WarehouseMapper warehouseMapper;

    public PageResponse<WarehouseResponse> getAll(int page, int size) {
        List<Warehouse> warehouses = warehouseRepository.findAll(page, size);

        long totalRecords = warehouseRepository.count();

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        return PageResponse.<WarehouseResponse>builder()
                .items(warehouses.stream().map(warehouseMapper::toWarehouseResponse).toList())
                .records(totalRecords)
                .totalPages(totalPages)
                .page(page)
                .build();
    }

    public WarehouseResponse getById(Long id) {
        Optional<Warehouse> optionalWarehouse = Optional.ofNullable(warehouseRepository.findById(id));

        if (optionalWarehouse.isEmpty()) {
            throw new AppException(ErrorCode.WAREHOUSE_NOTFOUND);
        }

        return warehouseMapper.toWarehouseResponse(optionalWarehouse.get());
    }

    public WarehouseResponse create(WarehouseCreationRequest warehouseCreationRequest) {
        Warehouse warehouse = warehouseMapper.toWarehouse(warehouseCreationRequest);

        long warehouseId = warehouseRepository.save(warehouse);
        warehouse.setId(warehouseId);

        return warehouseMapper.toWarehouseResponse(warehouse);
    }

    public WarehouseResponse update(long id, WarehouseUpdateRequest warehouseUpdateRequest) {
        Optional<Warehouse> optionalWarehouse = Optional.ofNullable(warehouseRepository.findById(id));

        if (optionalWarehouse.isEmpty()) {
            throw new AppException(ErrorCode.WAREHOUSE_NOTFOUND);
        }

        Warehouse warehouse = optionalWarehouse.get();

        warehouseMapper.updateWarehouse(warehouse, warehouseUpdateRequest);

        warehouseRepository.update(warehouse);

        return warehouseMapper.toWarehouseResponse(warehouse);
    }

    public void delete(Long id) {
        if (!warehouseRepository.existsById(id)) {
            throw new AppException(ErrorCode.WAREHOUSE_NOTFOUND);
        }

        warehouseRepository.deleteById(id);
    }
}
