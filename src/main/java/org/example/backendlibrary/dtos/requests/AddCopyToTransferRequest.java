package org.example.backendlibrary.dtos.requests;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCopyToTransferRequest {
    private Long copyId;
}
