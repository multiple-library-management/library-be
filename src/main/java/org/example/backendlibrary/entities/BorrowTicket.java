package org.example.backendlibrary.entities;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BorrowTicket {
    private Long id;

    private Long memberId;
    private Long librarianId;
}
