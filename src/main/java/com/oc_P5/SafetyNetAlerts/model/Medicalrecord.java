package com.oc_P5.SafetyNetAlerts.model;

import lombok.Getter;

import java.util.List;

@Getter
public class Medicalrecord {

    private String firstName;
    private String lastName;
    private String birthdate;
    private List<String> medications;
    private List<String> allergies;

}
