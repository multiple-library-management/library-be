package org.example.backendlibrary.entities;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Librarian {
    private Long libraryId;
    private Long employeeId;
}
