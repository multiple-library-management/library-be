package org.example.backendlibrary.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class WarehouseResponse {
    private Long id;
    private String name;

    private String address;
    private String district;
    private String ward;
    private String street;
    private String city;
}
