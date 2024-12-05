package org.example.backendlibrary.dtos.responses;

import java.security.Timestamp;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CopyBorrowTicketResponse {
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp returnDate;

    private Integer fee;
    private Integer fine;

    private String statusOnReturn;

    private Long copyId;
}
