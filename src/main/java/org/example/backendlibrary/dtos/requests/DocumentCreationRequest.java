package org.example.backendlibrary.dtos.requests;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DocumentCreationRequest {
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
