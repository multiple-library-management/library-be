package org.example.backendlibrary.entities;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Workshift {
    private Long id;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private Long employeeId;
}
