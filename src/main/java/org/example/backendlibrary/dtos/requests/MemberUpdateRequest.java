package org.example.backendlibrary.dtos.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MemberUpdateRequest {
    private String firstName;
    private String lastName;

    private String address;
    private String district;
    private String ward;
    private String street;
    private String city;

    private String phone;
    private String email;
}