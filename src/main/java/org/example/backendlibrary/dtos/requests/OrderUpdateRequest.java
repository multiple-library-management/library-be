package org.example.backendlibrary.dtos.requests;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class OrderUpdateRequest {
    private Timestamp createdDate;
    private Timestamp shipStartDate;
    private Timestamp shipEndDate;
    private Integer totalPrice;

    private Integer warehouseId;
    private Integer warehouseStaffId;
}
