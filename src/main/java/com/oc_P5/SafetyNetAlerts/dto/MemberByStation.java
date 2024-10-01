package com.oc_P5.SafetyNetAlerts.dto;

import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import lombok.Value;

import java.util.List;

@Value
public class MemberByStation {

    String lastName;
    String phone;
    int age;
    List<String> medications;
    List<String> allergies;

    public MemberByStation(Person person, MedicalRecord medicalRecord) {
        this.lastName = person.getLastName();
        this.phone = person.getPhone();
        this.age = medicalRecord.getAge();
        this.medications = medicalRecord.getMedications();
        this.allergies = medicalRecord.getAllergies();
    }

}
