package org.example.backendlibrary.entities;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime shipStartDate;
    private LocalDateTime shipEndDate;
    private Integer totalPrice;

    private Integer warehouseId;
    private Integer warehouseStaffId;
}
