package org.example.backendlibrary.dtos.requests;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkshiftUpdateRequest {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private Long employeeId;
}
