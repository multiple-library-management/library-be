package org.example.backendlibrary.entities;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Copy {
    private Long id;

    private Integer fee;
    private String status;

    private Long documentId;
    private Long libraryId;
    private Long warehouseId;
    private Long orderId;
}
