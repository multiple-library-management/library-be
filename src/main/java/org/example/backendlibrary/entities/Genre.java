package org.example.backendlibrary.entities;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Genre {
    private Long id;
    private String name;
}
