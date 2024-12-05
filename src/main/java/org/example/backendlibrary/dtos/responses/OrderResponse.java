package org.example.backendlibrary.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
