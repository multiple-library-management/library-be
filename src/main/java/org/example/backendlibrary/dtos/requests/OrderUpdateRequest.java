package org.example.backendlibrary.dtos.requests;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderUpdateRequest {
    private LocalDateTime shipStartDate;
    private LocalDateTime shipEndDate;
    private Integer totalPrice;

    private Integer warehouseId;
    private Integer warehouseStaffId;
}
