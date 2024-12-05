package org.example.backendlibrary.dtos.requests;

import java.sql.Timestamp;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCopyToBorrowTicketRequest {
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp returnDate;

    private Integer fee;
    private Integer fine;

    private String statusOnReturn;

    private Long copyId;
}
