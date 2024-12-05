package org.example.backendlibrary.dtos.responses;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderResponse {
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime shipStartDate;
    private LocalDateTime shipEndDate;
    private Integer totalPrice;

    private Integer warehouseId;
    private Integer warehouseStaffId;
}
