package com.oc_P5.SafetyNetAlerts.model;

import lombok.Getter;

import java.util.List;

public class Medicalrecord {

    @Getter
    private String firstName;
    @Getter
    private String lastName;
    @Getter
    private String birthdate;
    @Getter
    private List<String> medications;
    @Getter
    private List<String> allergies;

}
