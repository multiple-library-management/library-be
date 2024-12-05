package org.example.backendlibrary.dtos.responses;

import java.time.LocalDateTime;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponse {
    private Long id;

    private LocalDateTime createdDate;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Integer amount;
    private String type;

    private Long libraryId;
    private Long warehouseId;
    private Long warehouseStaffId;
}
