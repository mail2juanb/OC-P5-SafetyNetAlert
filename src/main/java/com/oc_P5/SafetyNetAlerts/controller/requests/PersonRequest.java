package com.oc_P5.SafetyNetAlerts.controller.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Value;

@Value
public class PersonRequest {

    @NotBlank(message = "firstName shouldn't be null or blank")
    String firstName;

    @NotBlank(message = "lastName shouldn't be null or blank")
    String lastName;

    String address;

    String city;

    @Positive
    Integer zip;

    String phone;

    @Email(message = "email must be valid")
    String email;

}
