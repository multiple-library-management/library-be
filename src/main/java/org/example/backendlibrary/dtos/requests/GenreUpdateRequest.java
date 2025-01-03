package org.example.backendlibrary.dtos.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GenreUpdateRequest {
    private String name;
}
