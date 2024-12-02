package org.example.backendlibrary.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DocumentResponse {
    private Long id;
    private String title;
    private String language;
    private String image;
    private Integer price;
    private String publisherName;
    private String documentType;
    private Integer volume;
    private String frequency;
    private Integer edition;

    private List<String> authors;
    private List<String> genres;
}
