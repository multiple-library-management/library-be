package org.example.backendlibrary.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.*;

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
