package org.example.backendlibrary.entities;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseStaff {
    private Long warehouseId;
    private Long employeeId;
}
