package com.oc_P5.SafetyNetAlerts.service.data_reader;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.Medicalrecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataWrapperList {

    private List<Person> persons;

    @JsonProperty("firestations")
    private List<Firestation> fireStations;

    @JsonProperty("medicalrecords")
    private List<Medicalrecord> medicalRecords;

}
