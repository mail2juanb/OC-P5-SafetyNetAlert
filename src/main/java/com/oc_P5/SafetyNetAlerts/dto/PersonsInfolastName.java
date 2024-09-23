package com.oc_P5.SafetyNetAlerts.dto;


import com.oc_P5.SafetyNetAlerts.model.MedicalRecord;
import com.oc_P5.SafetyNetAlerts.model.Person;
import lombok.Value;

import java.util.List;

@Value
public class PersonsInfolastName {

    List<PersonInfolastName> personInfolastNameList;

    public PersonsInfolastName(List<PersonInfolastName> personInfolastNameList) {
        this.personInfolastNameList = personInfolastNameList;
    }

    @Value
    // mise en forme : String lastName,String address, String phone, int age, List<String>medications, List<String>allergies
    public static class PersonInfolastName {
        String lastName;
        String address;
        String phone;
        int age;
        List<String> medications;
        List<String> allergies;

        public PersonInfolastName(Person person, MedicalRecord medicalRecord) {
            this.lastName = person.getLastName();
            this.address = person.getAddress();
            this.phone = person.getPhone();
            this.age = medicalRecord.getAge();
            this.medications = medicalRecord.getMedications();
            this.allergies = medicalRecord.getAllergies();
        }
    }

}
