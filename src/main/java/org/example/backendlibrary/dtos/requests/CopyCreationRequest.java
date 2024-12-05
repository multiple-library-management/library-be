package org.example.backendlibrary.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CopyCreationRequest {
    private Integer fee;
    private String status;

    private Long documentId;
    private Long libraryId;
    private Long warehouseId;
    private Long orderId;
}
