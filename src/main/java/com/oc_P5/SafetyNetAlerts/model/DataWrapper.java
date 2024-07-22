package com.oc_P5.SafetyNetAlerts.model;

import lombok.Getter;

import java.util.List;

public class DataWrapper {

    @Getter
    private List<Person> persons;
    @Getter
    private List<Firestation> firestations;
    @Getter
    private List<Medicalrecord> medicalrecords;

}
