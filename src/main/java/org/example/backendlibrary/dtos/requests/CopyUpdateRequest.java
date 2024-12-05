package org.example.backendlibrary.dtos.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CopyUpdateRequest {
    private Integer fee;
    private String status;

    private Long documentId;
    private Long libraryId;
    private Long warehouseId;
    private Long orderId;
}
