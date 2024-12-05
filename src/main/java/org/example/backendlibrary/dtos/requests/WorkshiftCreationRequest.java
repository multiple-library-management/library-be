package org.example.backendlibrary.dtos.requests;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkshiftCreationRequest {
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private Long employeeId;
}
