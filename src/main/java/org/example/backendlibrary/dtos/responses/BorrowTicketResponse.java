package org.example.backendlibrary.dtos.responses;

import java.util.List;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowTicketResponse {
    private Long id;

    private Long memberId;
    private Long librarianId;

    private List<CopyBorrowTicketResponse> copies;
}
