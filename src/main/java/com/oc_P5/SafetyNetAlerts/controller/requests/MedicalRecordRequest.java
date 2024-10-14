package com.oc_P5.SafetyNetAlerts.controller.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
public class MedicalRecordRequest {

    @NotBlank(message = "firstName shouldn't be null or blank")
    String firstName;

    @NotBlank(message = "lastName shouldn't be null or blank")
    String lastName;

    @Past(message = "Birthdate must be in the past")
    LocalDate birthdate;

    List<String> medications;

    List<String> allergies;

}
