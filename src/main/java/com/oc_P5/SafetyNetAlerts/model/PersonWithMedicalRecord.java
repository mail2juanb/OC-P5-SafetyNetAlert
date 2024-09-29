package com.oc_P5.SafetyNetAlerts.model;

public record PersonWithMedicalRecord(Person person, MedicalRecord medicalRecord){
    public String getFirstName() {
        return person.getFirstName();
    }
    public String getLastName() {
        return person.getLastName();
    }
}