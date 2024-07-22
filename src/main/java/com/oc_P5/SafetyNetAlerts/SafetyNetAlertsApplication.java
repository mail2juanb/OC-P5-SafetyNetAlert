package com.oc_P5.SafetyNetAlerts;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.oc_P5.SafetyNetAlerts.model.DataWrapper;
import com.oc_P5.SafetyNetAlerts.model.Firestation;
import com.oc_P5.SafetyNetAlerts.model.Medicalrecord;
import com.oc_P5.SafetyNetAlerts.model.Person;

import java.io.File;
import java.io.IOException;

public class SafetyNetAlertsApplication {

	public static void main(String[] args) {
		ObjectMapper objectMapper = new ObjectMapper();

		try {

			// Lire le fichier JSON et le convertir en objet DataWrapper
			DataWrapper dataWrapper = objectMapper.readValue(new File("src/main/resources/data.json"), DataWrapper.class);

			// Afficher les details de l'objet désérialisé du node persons
			for (Person person : dataWrapper.getPersons()) {
				System.out.println("Name: " + person.getFirstName() + " " + person.getLastName());
				System.out.println("Address: " + person.getAddress());
				System.out.println("City: " + person.getCity());
				System.out.println("Zip: " + person.getZip());
				System.out.println("Phone: " + person.getPhone());
				System.out.println("Email: " + person.getEmail());
				System.out.println();
			}

			// Afficher les details de l'objet désérialisé du node firestations
			for (Firestation firestations : dataWrapper.getFirestations()) {
				System.out.println("Address: " + firestations.getAddress());
				System.out.println("Station: " + firestations.getStation());
				System.out.println();
			}

			// Afficher les details de l'objet désérialisé du node medicalrecords
			for (Medicalrecord medicalrecord : dataWrapper.getMedicalrecords()) {
				System.out.println("Name: " + medicalrecord.getFirstName() + " " + medicalrecord.getLastName());
				System.out.println("Birthdate: " + medicalrecord.getBirthdate());
				System.out.println("Medications: " + String.join(", ", medicalrecord.getMedications()));
				System.out.println("Allergies: " + String.join(", ", medicalrecord.getAllergies()));
				System.out.println();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
