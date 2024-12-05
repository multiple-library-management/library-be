package org.example.backendlibrary.entities;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CopyTransfer {
    private Long copyId;
    private Long transferId;
}
