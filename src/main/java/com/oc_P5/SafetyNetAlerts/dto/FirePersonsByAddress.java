package com.oc_P5.SafetyNetAlerts.dto;

import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import lombok.Value;

import java.util.List;

@Value
public class FirePersonsByAddress {

    List<FirePersonByAddress> firePersonByAdressList;
    String address;
    Integer stationNumber;

    public FirePersonsByAddress(String address, Integer stationNumber, List<FirePersonByAddress> firePersonByAdressList) {
        this.address = address;
        this.stationNumber = stationNumber;
        this.firePersonByAdressList = firePersonByAdressList;
    }

    @Value
    public static class FirePersonByAddress {
        String lastName;
        String phone;
        int age;
        List<String> medications;
        List<String> allergies;

        // Constructeur pour initialiser l'objet FirePersonsByAddress
        public FirePersonByAddress(Person person, MedicalRecord medicalRecord) {
            this.lastName = person.getLastName();
            this.phone = person.getPhone();
            this.age = medicalRecord.getAge();
            this.medications = medicalRecord.getMedications();
            this.allergies = medicalRecord.getAllergies();
        }
    }

}