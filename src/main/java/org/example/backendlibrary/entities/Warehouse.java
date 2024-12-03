package org.example.backendlibrary.entities;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {
    private Long id;
    private String name;

    private String address;
    private String district;
    private String ward;
    private String street;
    private String city;
}
