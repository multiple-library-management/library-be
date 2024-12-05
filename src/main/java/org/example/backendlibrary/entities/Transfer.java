package org.example.backendlibrary.entities;

import java.sql.Timestamp;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transfer {
    private Long id;

    private Timestamp createdDate;
    private Timestamp startDate;
    private Timestamp endDate;

    private Integer amount;
    private String type;

    private Long libraryId;
    private Long warehouseId;
    private Long warehouseStaffId;
}
