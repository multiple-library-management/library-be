package org.example.backendlibrary.dtos.requests;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowTicketCreationRequest {
    private Long memberId;
    private Long librarianId;
}
