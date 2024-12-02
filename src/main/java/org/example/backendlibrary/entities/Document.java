package org.example.backendlibrary.entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document {
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
