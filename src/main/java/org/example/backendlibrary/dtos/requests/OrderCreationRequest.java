package org.example.backendlibrary.dtos.requests;

import java.sql.Timestamp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderCreationRequest {
    private Timestamp createdDate;
    private Timestamp shipStartDate;
    private Timestamp shipEndDate;

    private Integer warehouseId;
    private Integer warehouseStaffId;
}
