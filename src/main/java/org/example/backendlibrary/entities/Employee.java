package org.example.backendlibrary.entities;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
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
}
