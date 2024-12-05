package org.example.backendlibrary.dtos.requests;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferUpdateRequest {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Integer amount;
    private String type;

    private Long libraryId;
    private Long warehouseId;
    private Long warehouseStaffId;
}
