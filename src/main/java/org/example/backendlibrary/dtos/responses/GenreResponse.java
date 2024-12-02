package org.example.backendlibrary.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GenreResponse {
    private Long id;
    private String name;
}
