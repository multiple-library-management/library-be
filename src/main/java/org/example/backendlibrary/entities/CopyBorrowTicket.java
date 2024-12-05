package org.example.backendlibrary.entities;

import java.sql.Timestamp;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CopyBorrowTicket {
    private Timestamp startDate;
    private Timestamp endDate;
    private Timestamp returnDate;

    private Integer fee;
    private Integer fine;

    private String statusOnReturn;

    private Long copyId;
    private Long borrowTicketId;
}
