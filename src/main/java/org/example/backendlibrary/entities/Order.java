package org.example.backendlibrary.entities;

import java.sql.Timestamp;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private Timestamp createdDate;
    private Timestamp shipStartDate;
    private Timestamp shipEndDate;
    private Integer totalPrice;

    private Integer warehouseId;
    private Integer warehouseStaffId;
}
