package org.example.backendlibrary.dtos.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class OrderCreationRequest {
    private LocalDateTime shipStartDate;
    private LocalDateTime shipEndDate;

    private Integer warehouseId;
    private Integer warehouseStaffId;
}
