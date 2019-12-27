package com.example.azure.persistence.models;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AddressModel {

    private String addressType;
    private String line1;
    private String line2;
    private String city;
    private String country;
}
