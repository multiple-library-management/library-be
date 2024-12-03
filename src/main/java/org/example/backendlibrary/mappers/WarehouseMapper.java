package org.example.backendlibrary.mappers;

import org.example.backendlibrary.dtos.requests.WarehouseCreationRequest;
import org.example.backendlibrary.dtos.requests.WarehouseUpdateRequest;
import org.example.backendlibrary.dtos.responses.WarehouseResponse;
import org.example.backendlibrary.entities.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WarehouseMapper {
    WarehouseResponse toWarehouseResponse(Warehouse warehouse);

    Warehouse toWarehouse(WarehouseCreationRequest warehouseCreationRequest);

    void updateWarehouse(@MappingTarget Warehouse warehouse, WarehouseUpdateRequest warehouseUpdateRequest);
}
