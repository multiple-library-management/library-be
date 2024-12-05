package org.example.backendlibrary.dtos.requests;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderCreationRequest {
    private LocalDateTime shipStartDate;
    private LocalDateTime shipEndDate;

    private Integer warehouseId;
    private Integer warehouseStaffId;
}
