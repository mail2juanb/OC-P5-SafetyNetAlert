package com.oc_P5.SafetyNetAlerts.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class DataWrapper {

    private List<Person> persons;

    @JsonProperty("firestations")
    private List<Firestation> fireStations;

    @JsonProperty("medicalrecords")
    private List<Medicalrecord> medicalRecords;

}
