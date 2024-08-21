package com.oc_P5.SafetyNetAlerts;


import com.oc_P5.SafetyNetAlerts.service.DataLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class SafetyNetAlertsApplication {

    public static void main(String[] args) throws Exception {

        SpringApplication.run(SafetyNetAlertsApplication.class, args);

        DataLoader dataLoader = new DataLoader();
        dataLoader.setDataWrapperList();

        /*
        // Afficher les details de l'objet désérialisé du node persons
        for (Person person : dataWrapper.getPersons()) {
            log.debug("Name: " + person.getFirstName() + " " + person.getLastName());
            log.debug("Address: " + person.getAddress());
            log.debug("City: " + person.getCity());
            log.debug("Zip: " + person.getZip());
            log.debug("Phone: " + person.getPhone());
            log.debug("Email: " + person.getEmail());
        }

        // Afficher les details de l'objet désérialisé du node firestations
        for (Firestation firestations : dataWrapper.getFirestations()) {
            log.debug("Address: " + firestations.getAddress());
            log.debug("Station: " + firestations.getStation());
        }

        // Afficher les details de l'objet désérialisé du node medicalrecords
        for (Medicalrecord medicalrecord : dataWrapper.getMedicalrecords()) {
            log.debug("Name: " + medicalrecord.getFirstName() + " " + medicalrecord.getLastName());
            log.debug("Birthdate: " + medicalrecord.getBirthdate());
            log.debug("Medications: " + String.join(", ", medicalrecord.getMedications()));
            log.debug("Allergies: " + String.join(", ", medicalrecord.getAllergies()));
        }
        */
    }

}
