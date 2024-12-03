package org.example.backendlibrary.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmployeeResponse {
    private Long id;

    private String firstName;
    private String lastName;

    private String address;
    private String district;
    private String ward;
    private String street;
    private String city;

    private String phone;
    private String email;
    private Integer salary;

    private String type;
    private Long libraryId;
    private Long warehouseId;
}
