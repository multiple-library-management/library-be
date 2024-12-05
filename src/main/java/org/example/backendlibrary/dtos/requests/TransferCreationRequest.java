package org.example.backendlibrary.dtos.requests;

import java.sql.Timestamp;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferCreationRequest {
    private Timestamp createdDate;
    private Timestamp startDate;
    private Timestamp endDate;

    private Integer amount;
    private String type;

    private Long libraryId;
    private Long warehouseId;
    private Long warehouseStaffId;
}
