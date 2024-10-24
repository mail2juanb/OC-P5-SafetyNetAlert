package com.oc_P5.SafetyNetAlerts.dto;


import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import lombok.Value;

import java.util.List;

@Value
public class PersonInfoLastName {

    String lastName;
    String address;
    String phone;
    int age;
    List<String> medications;
    List<String> allergies;

    public PersonInfoLastName(Person person, MedicalRecord medicalRecord) {
        this.lastName = person.getLastName();
        this.address = person.getAddress();
        this.phone = person.getPhone();
        this.age = medicalRecord.getAge();
        this.medications = medicalRecord.getMedications();
        this.allergies = medicalRecord.getAllergies();
    }
}
